package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ClimberCommand extends Command{
	private double power;
	public ClimberCommand(double power)
	{
		super("ClimberCommand");
		requires(Robot.climber);
		this.power = power;
	}

	// Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climber.talon1.set(-power);
    	Robot.climber.talon1.set(-power);
//    	if (!Robot.oi.shiftButton.get())
//    	{
//        	Robot.climber.talon1.set(0);
//        	Robot.climber.talon2.set(0);
//        	end();
//    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.climber.talon1.set(0);
    	Robot.climber.talon2.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

}
