package org.usfirst.frc.team2907.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team2907.robot.Robot;
import org.usfirst.frc.team2907.robot.RobotMap;
import org.usfirst.frc.team2907.robot.commands.ReadCommand;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Camera extends Subsystem
{
	public static final int MAX_BLOCKS = 20;
	public static final int BLOCK_SIZE = 14;
	// private SPI port;
	private I2C port;
	private boolean inRange;
	private double offset;
	
	private ArrayList<PixyBlock> pixyBlocks = new ArrayList<>();

	public Camera()
	{
		try
		{
			port = new I2C(I2C.Port.kOnboard, 0x54);
			// port = new SPI(SPI.Port.kOnboardCS0);
			// port.setSampleDataOnFalling();
		} catch (Exception e)
		{
			System.out.println("e : " + e.getLocalizedMessage());
		}
		// read();
	}

	public void initDefaultCommand()
	{
		setDefaultCommand(new ReadCommand());
	}
	
	public void setLastOffset(double offset)
	{
		this.offset = offset;
		setInRange(true);
	}
	
	public double getLastOffset()
	{
		return offset;
	}

	public void setInRange(boolean inRange)
	{
		this.inRange = inRange;
	}
	
	public double getDistance(double width)
	{
		double distance = (1.166 * 320) / (2 * width * Math.tan(75 / 2));
		System.out.println("Width : " + width + " distance : " + distance);
		return distance;
	}

	public ArrayList<PixyBlock> read()
	{
		pixyBlocks.clear();
//		ArrayList<PixyBlock> pixyBlocks = new ArrayList<>();
//		PixyBlock[] pixyBlocks = new PixyBlock[MAX_BLOCKS];
//		int pixyIndex = 0;
		byte[] bytes = new byte[64];
		port.read(0x54, 64, bytes);
		// for (int i = 0; i < bytes.length; ++i)
		// {
		// if ((int)bytes[i] != 0)
		// System.out.println("Byte : " + bytes[i]);
		// }
		int index = 0;
		for ( ; index < bytes.length - 1; ++index)
		{
			int b1 = bytes[index];
			if (b1 < 0)
				b1 += 256;

			int b2 = bytes[index + 1];
			if (b2 < 0)
				b2 += 256;

			if (b1 == 0x55 && b2 == 0xaa) 
				break;
		}
		
		if (index == 63)
			return null;
		else if (index == 0) 
			index += 2;
//		for (int i = 0; i < bytes.length; ++i)
//		{
//			if ((int)bytes[i] != 0)
//				System.out.println("Byte : " + bytes[i]);
//		}
		// int result = port.read(true, bytes, BLOCK_SIZE * MAX_BLOCKS);
		// System.out.println("bytes read : " + result);
		// System.out.println("Bytes read : " + bytes);
		System.out.println("-----------------");
		for (int byteOffset = index; byteOffset < bytes.length - BLOCK_SIZE - 1;)
		{
			// checking for sync block
			int b1 = bytes[byteOffset];
			if (b1 < 0)
				b1 += 256;

			int b2 = bytes[byteOffset + 1];
			if (b2 < 0)
				b2 += 256;

			// System.out.println("byte : " + b1); //bytes[byteOffset]);
			if (b1 == 0x55 && b2 == 0xaa)
			{
				// System.out.println("\n" + bytes[byteOffset]);
				//System.out.println("\n" + bytes[byteOffset]);
				// copy block into temp buffer
				byte[] temp = new byte[BLOCK_SIZE];
				StringBuilder sb = new StringBuilder("Data : ");
				for (int tempOffset = 0; tempOffset < BLOCK_SIZE; ++tempOffset)
				{
					temp[tempOffset] = bytes[byteOffset + tempOffset];
					sb.append(temp[tempOffset] + ", ");
					// System.out.println("read byte : " + temp[tempOffset]);
				}
				// System.out.println(sb.toString());
				//System.out.println(sb.toString());

				PixyBlock block = bytesToBlock(temp);
				if (block != null)
				{
					pixyBlocks.add(block);
//					pixyBlocks[pixyIndex++] = block;
					System.out.println("Block width : " + block.width + ", block height : " + block.height);
					System.out.println("Block x : " + block.centerX + ", block y : " + block.centerY);
					System.out.println("Sig : " + block.signature);
					System.out.println("checksum : " + block.checksum);
					byteOffset += BLOCK_SIZE - 1;
				} else
					++byteOffset;
			} else
				++byteOffset;
		}
		
		if (pixyBlocks != null && pixyBlocks.size() > 0)
		{
			if (pixyBlocks.size() >= 2)
			{
				PixyBlock leftBlock;
				PixyBlock rightBlock;
				if (pixyBlocks.get(0).centerX > pixyBlocks.get(1).centerX)
				{
					leftBlock = pixyBlocks.get(1);
					rightBlock = pixyBlocks.get(0);
				} else 
				{
					leftBlock = pixyBlocks.get(0);
					rightBlock = pixyBlocks.get(1);
				}
				double difference = (rightBlock.centerX + leftBlock.centerX) / 2;
				System.out.println("Center X : " + difference);
				Robot.camera.setLastOffset(difference);
				double total = (rightBlock.centerX + rightBlock.width / 2) - (leftBlock.centerX - leftBlock.width / 2);
				getDistance(total);
			} else 
			{
				setLastOffset(pixyBlocks.get(0).centerX);
			}
		} else 
		{
			setInRange(false);
		}
		return pixyBlocks;
	}

	public PixyBlock bytesToBlock(byte[] bytes)
	{
		PixyBlock pixyBlock = new PixyBlock();
		pixyBlock.sync = bytesToInt(bytes[1], bytes[0]);
		pixyBlock.checksum = bytesToInt(bytes[3], bytes[2]);

		// if the checksum is 0 or the checksum is a sync byte, then there
		// are no more frames.
		// if (pixyBlock.checksum == 0 || pixyBlock.checksum == 0xaa55)
		// return null;

		// pixyBlock.signature = bytesToInt(bytes[5], bytes[4]);
		// pixyBlock.centerX = bytesToInt(bytes[7], bytes[6]);
		// pixyBlock.centerY = bytesToInt(bytes[9], bytes[8]);
		// pixyBlock.width = bytesToInt(bytes[11], bytes[10]);
		// pixyBlock.height = bytesToInt(bytes[13], bytes[12]);

		// System.out.println("centerx byte b1 : " + bytes[7] + ", b2 : "
		// + bytes[6]);

		pixyBlock.signature = orBytes(bytes[5], bytes[4]);
		pixyBlock.centerX = ((((int) bytes[7] & 0xff) << 8) | ((int) bytes[6] & 0xff));
		pixyBlock.centerY = ((((int) bytes[9] & 0xff) << 8) | ((int) bytes[8] & 0xff));
		pixyBlock.width = ((((int) bytes[11] & 0xff) << 8) | ((int) bytes[10] & 0xff));
		pixyBlock.height = ((((int) bytes[13] & 0xff) << 8) | ((int) bytes[12] & 0xff));
		return pixyBlock;
	}

	public int orBytes(byte b1, byte b2)
	{
		return (b1 & 0xff) | (b2 & 0xff);
	}

	public int bytesToInt(int b1, int b2)
	{
		if (b1 < 0)
			b1 += 256;

		if (b2 < 0)
			b2 += 256;

		int intValue = b1 * 256;
		intValue += b2;
		return intValue;
	}

	public class PixyBlock
	{
		// 0, 1 0 sync (0xaa55)
		// 2, 3 1 checksum (sum of all 16-bit words 2-6)
		// 4, 5 2 signature number
		// 6, 7 3 x center of object
		// 8, 9 4 y center of object
		// 10, 11 5 width of object
		// 12, 13 6 height of object

		// read byte : 85 read byte : -86
		// read byte : 85 read byte : -86
		// read byte : 22 read byte : 1
		// read by
		// read byte : -128 read byte : 0
		// read byte : 118 read byte : 0
		// read byte : 22 read byte : 0

		public int sync;
		public int checksum;
		public int signature;
		public int centerX;
		public int centerY;
		public int width;
		public int height;
	}
}
