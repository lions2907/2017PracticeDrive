package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TimeDriveCommand extends Command
{

	public TimeDriveCommand(double runTime)
	{
		super(runTime);
		requires(Robot.driveTrain);

	}

	@Override
	protected void execute()
	{
		Robot.driveTrain.robotDrive.arcadeDrive(1, 0);
	}

	@Override
	protected void interrupted()
	{
		end();
	}

	@Override
	protected void end()
	{
		Robot.driveTrain.robotDrive.arcadeDrive(0,0);
	}

	@Override
	protected boolean isFinished()
	{
		// TODO Auto-generated method stub
		return isTimedOut();
	}

}
