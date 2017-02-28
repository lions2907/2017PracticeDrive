package org.usfirst.frc.team2907.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team2907.robot.commands.ReadCommand;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CameraManager extends Subsystem
{
	/* GLOBAL CONSTANTS */
	public static final double IMAGE_WIDTH = 320.0;
	public static final int BLOCK_SIZE = 14;
	public static final int BUFFER_SIZE = 64;
	/* RESUSED FIELDS */
	private byte[] bytes;
	/* GEAR CAMERA CONSTANTS */
	public static final double PIXY_POV_GEAR = 75; 
	public static final double GEAR_WIDTH_FT = 1.166;
	public static final double DEGREES_PER_PIXEL_GEAR = PIXY_POV_GEAR / IMAGE_WIDTH;
	public static int PIXY_ADDRESS_GEAR = 0x54;
	/* GEAR CAMERA FIELDS */
	private I2C gearCamera;
	private boolean gearInRange;
	private double gearOffset;
	private ArrayList<PixyBlock> gearBlocks;
	/* TOWER CAMERA CONSTANTS */
	public static final double PIXY_POV_TOWER = 42;
	public static int PIXY_ADDRESS_TOWER = 0x55;
	public static final double TOWER_WIDTH_FT = 0;
	public static final double DEGREES_PER_PIXEL_TOWER = PIXY_POV_TOWER / IMAGE_WIDTH;
	/* TOWER CAMERA FIELDS */
	private I2C towerCamera;
	private ArrayList<PixyBlock> towerBlocks;
	private double towerXOffset;
	private double towerYOffset;
	private boolean towerInRange;
	private Servo towerServo = new Servo(0);
	private boolean servoStatus = false;
	
	public CameraManager()
	{
		gearCamera = new I2C(I2C.Port.kOnboard/*RIO I2C*/, PIXY_ADDRESS_GEAR);
		towerCamera = new I2C(I2C.Port.kMXP/*NAVX MXP I2C*/, PIXY_ADDRESS_TOWER);
	}
	
	@Override
	public void initDefaultCommand()
	{
		setDefaultCommand(new ReadCommand());
	}
	
//	public double getDistance(double width, double targetCenter)
//	{
//		double distance = (GEAR_WIDTH_FT * IMAGE_WIDTH) / (2 * width * Math.tan(PIXY_POV_GEAR / 2));
//		System.out.println("Width : " + width + " distance : " + distance);
//		double angleToTarget = (IMAGE_WIDTH / 2 - targetCenter) * DEGREES_PER_PIXEL;
//		double sideDistance = distance * Math.sin(angleToTarget);
//		System.out.println("Angle : " + angleToTarget + " sideDistance : " + sideDistance);
//		return distance;
//	}
	
//	public double getTowerDistance(double width, double targetCenter)
//	{
//		System.out.println("UNIMPLEMENTED DISTANCE");
//		
//		double distance = (TOWER_WIDTH_FT * IMAGE_WIDTH) / (2.0 * width * Math.tan(PIXY_POV_TOWER / 2.0));
//		System.out.println("Width : " + width + " distance : " + distance);
//		double angleToTarget = (IMAGE_WIDTH / 2.0 - targetCenter) * DEGREES_PER_PIXEL_TOWER;
//		double sideDistance = distance * Math.sin(angleToTarget);
//		System.out.println("Angle : " + angleToTarget + " sideDistance : " + sideDistance);
//		return distance;
//	}
	
	public void readCameras()
	{
		//System.out.println("---------- READING GEAR CAMERA ----------");
//		gearRead();
		//System.out.println("---------- END GEAR CAMERA ---------- \n");
		//System.out.println("---------- READING TOWER CAMERA ----------");
		towerRead();
		//System.out.println("---------- END TOWER CAMERA ---------- \n");
	}
	
	private void towerRead()
	{
		if (towerBlocks == null) // lazy instantiation
			towerBlocks = new ArrayList<>();
			
		towerBlocks.clear(); // don't leave previous blocks in
		read(towerBlocks, towerCamera); // read from tower camera and store result into tower block array list
		
		if (towerBlocks.size() > 0)
		{
			PixyBlock largerBlock;
			if (towerBlocks.size() == 1)
			{
				largerBlock = towerBlocks.get(0);
			} else if ((towerBlocks.get(0).width * towerBlocks.get(0).height) > (towerBlocks.get(1).width * towerBlocks.get(1).height))
			{
				largerBlock = towerBlocks.get(0);
			} else 
			{
				largerBlock = towerBlocks.get(1);
			}
			
			towerXOffset = largerBlock.centerX;
			towerYOffset = largerBlock.centerY;
			System.out.println("tower x " + towerXOffset);
			System.out.println("tower y " + towerYOffset);
			towerInRange = true;
		} else 
			towerInRange = false;
	}
	
	private void gearRead()
	{
		if (gearBlocks == null)
			gearBlocks = new ArrayList<>();
			
		gearBlocks.clear();
		gearBlocks = read(gearBlocks, gearCamera);
		
		if (gearBlocks.size() > 0) // found something!
		{
			System.out.println();
			if (gearBlocks.size() >= 2) // prob found the gear stand!
			{
				PixyBlock leftBlock;
				PixyBlock rightBlock;
				if (gearBlocks.get(0).centerX > gearBlocks.get(1).centerX)
				{
					leftBlock = gearBlocks.get(1);
					rightBlock = gearBlocks.get(0);
				} else
				{
					leftBlock = gearBlocks.get(0);
					rightBlock = gearBlocks.get(1);
				}
				double difference = (rightBlock.centerX + leftBlock.centerX) / 2;
				System.out.println("Center X : " + difference);
				setGearOffset(difference);
				double total = (rightBlock.centerX) - (leftBlock.centerX);
				//getDistance(total, difference);
			} else
			{
				setGearOffset(gearBlocks.get(0).centerX);
			}
		} else
		{
			gearInRange = false;
		}
	}
	
	private ArrayList<PixyBlock> read(ArrayList<PixyBlock> blocks, I2C camera)
	{
		bytes = new byte[BUFFER_SIZE];
		camera.read(0x54, BUFFER_SIZE, bytes);
		
		int index = 0;
		for (; index < bytes.length - 1; ++index)
		{
			int b1 = bytes[index];
			if (b1 < 0)
				b1 += 256;
			
			int b2 = bytes[index + 1];
			if (b2 < 0)
				b2 += 256;
			
			if (b1 == 0x55 && b2 == 0xaa) // searching for header
				break;
		}
		
		if (index == BUFFER_SIZE - 1) // no header found
			return blocks;
		else if (index == 0) // header found at start index
			index += 2;
		
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
			
			if (b1 == 0x55 && b2 == 0xaa)
			{
				// copy block into temp buffer
				byte[] temp = new byte[BLOCK_SIZE];
				//				StringBuilder sb = new StringBuilder("Data : ");
				for (int tempOffset = 0; tempOffset < BLOCK_SIZE; ++tempOffset)
				{
					temp[tempOffset] = bytes[byteOffset + tempOffset];
					//					sb.append(temp[tempOffset] + ", ");
					// System.out.println("read byte : " + temp[tempOffset]);
				}
				// System.out.println(sb.toString());
				
				PixyBlock block = bytesToBlock(temp);
				if (block != null)
				{
					blocks.add(block);
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
		return blocks;
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
		
		public int sync;
		public int checksum;
		public int signature;
		public int centerX;
		public int centerY;
		public int width;
		public int height;
	}
	
	public void setGearOffset(double offset)
	{
		gearOffset = offset;
		gearInRange = true;
	}
	
	public double getGearOffset()
	{
		return gearOffset;
	}
	
	public double getTowerXOffset()
	{
		return towerXOffset;
	}
	
	public double getTowerYOffset()
	{
		return towerYOffset;
	}
	
	public boolean isTowerInRange()
	{
		return towerInRange;
	}
	
	public void turnSidewayTower()
	{
		towerServo.setAngle(90);
		// TODO implement servo turning
	}
	
	public void turnStraightTower()
	{
		towerServo.setAngle(0);
		// TODO implement servo turning
	}
	
	public void turnServo()
	{
		if (servoStatus)
		{
			turnSidewayTower();
		} else 
		{
			turnStraightTower();
		}
		
		servoStatus = !servoStatus;
	}
	
	public PIDSource gearCameraPID = new PIDSource()
	{

		@Override
		public void setPIDSourceType(PIDSourceType pidSource)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public PIDSourceType getPIDSourceType()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double pidGet()
		{
			// TODO Auto-generated method stub
			return gearOffset;
		}
		
	};
	
	public PIDSource towerCameraPID = new PIDSource()
	{

		@Override
		public void setPIDSourceType(PIDSourceType pidSource)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public PIDSourceType getPIDSourceType()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double pidGet()
		{
			// TODO Auto-generated method stub
			return 0;
		}
		
	};
	
}