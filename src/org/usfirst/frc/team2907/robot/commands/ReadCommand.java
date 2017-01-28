package org.usfirst.frc.team2907.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team2907.robot.Robot;
import org.usfirst.frc.team2907.robot.subsystems.Camera.PixyBlock;

import edu.wpi.first.wpilibj.command.Command;

public class ReadCommand extends Command{

	private static final double DEGREES_PER_PIXEL = 75.0 / 320.0;
	public ReadCommand()
	{
		super("ReadCommand");
		requires(Robot.camera);
	}
	
	protected void execute()
	{
		Robot.camera.read();
//		ArrayList<PixyBlock> blocks = Robot.camera.read();
//		PixyBlock[] blocks = Robot.camera.read();
//		if (blocks != null && blocks.size() > 0)
//		{
//			if (blocks.size() >= 2)
//			{
//				PixyBlock leftBlock;
//				PixyBlock rightBlock;
//				if (blocks.get(0).centerX > blocks.get(1).centerX)
//				{
//					leftBlock = blocks.get(1);
//					rightBlock = blocks.get(0);
//				} else 
//				{
//					leftBlock = blocks.get(0);
//					rightBlock = blocks.get(1);
//				}
//				double difference = (rightBlock.centerX + leftBlock.centerX) / 2;
//				System.out.println("Center X : " + difference);
//				Robot.camera.setLastOffset(difference);
//			}
//		} else 
//		{
//			Robot.camera.setInRange(false);
//		}
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
