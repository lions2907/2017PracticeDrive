package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ShootCommand extends Command
{
	public ShootCommand()
	{
		super("ShootCommand");
		requires(Robot.shooter);
	}
	
	public void execute()
	{
		Robot.shooter.shoot(1);
	}
	
	public void end()
	{
		Robot.shooter.shoot(0);
	}
	
	public void interrupted()
	{
		end();
	}

	@Override
	protected boolean isFinished()
	{
		return false;
	}
}
