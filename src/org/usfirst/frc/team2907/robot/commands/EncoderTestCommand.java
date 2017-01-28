package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class EncoderTestCommand extends Command
{
	private double start;
	public EncoderTestCommand()
	{
		super("Encoder Test");
		requires(Robot.climber);
		start = Robot.climber.climbEncoder.getDistance();
	}
	
	protected void initialize() 
	{
		Robot.climber.talon1.set(.5);
		Robot.climber.talon2.set(.5);
	}
	
	public void execute()
	{
		System.out.println("Distance : " + Robot.climber.climbEncoder.getDistance());
		Robot.climber.talon1.set(.2);
		Robot.climber.talon2.set(.2);
	}
	
	public void end()
	{
		Robot.climber.talon1.set(0);
		Robot.climber.talon2.set(0);
	}

	@Override
	protected boolean isFinished()
	{
		return Robot.climber.climbEncoder.getDistance() > (start + 2);
	}

}
