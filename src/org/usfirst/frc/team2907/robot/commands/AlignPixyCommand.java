package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AlignPixyCommand extends Command
{
	
	public AlignPixyCommand()
	{
		super("AlignPixy");
		requires(Robot.camera);
		requires(Robot.driveTrain);
	}

	protected void initialize()
	{
		double offset = Robot.camera.getLastOffset();
		if (offset > 160)
		{
			Robot.driveTrain.robotDrive.arcadeDrive(0, 0.5);
		} else 
		{
			Robot.driveTrain.robotDrive.arcadeDrive(0, -0.5);
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute()
	{
		Robot.camera.read();
		double offset = Robot.camera.getLastOffset();
		if (offset > 160)
		{
			Robot.driveTrain.robotDrive.arcadeDrive(0, 0.5);
		} else 
		{
			Robot.driveTrain.robotDrive.arcadeDrive(0, -0.5);
		}
		System.out.println("offset : " + offset);
		
	}

	@Override
	protected boolean isFinished()
	{
		double offset = Robot.camera.getLastOffset();
		//System.out.println("offset : " + offset);
		// TODO Auto-generated method stub
		return Math.abs(offset - 160) < 10;
	}

}
