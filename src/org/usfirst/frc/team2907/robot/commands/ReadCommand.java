package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;
import org.usfirst.frc.team2907.robot.subsystems.Camera.PixyBlock;

import edu.wpi.first.wpilibj.command.Command;

public class ReadCommand extends Command{

	public ReadCommand()
	{
		super("ReadCommand");
		requires(Robot.camera);
	}
	
	protected void execute()
	{
		PixyBlock[] blocks = Robot.camera.read();
		if (blocks != null && blocks.length > 0)
		{
			PixyBlock lastBlock = blocks[0];
			Robot.camera.setLastBlock(lastBlock);
		} else 
		{
			Robot.camera.setInRange(false);
		}
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
