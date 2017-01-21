package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArcadeDrive extends Command {

    public ArcadeDrive() {
    	requires(Robot.driveTrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	System.out.println("Twist : " + Robot.oi.leftStick.getTwist() + ", x : " + Robot.oi.leftStick.getX());
    	Robot.driveTrain.getRobotDrive().arcadeDrive(-Robot.oi.leftStick.getY(), Robot.oi.leftStick.getZ());
    	//Robot.driveTrain.extra.set(Robot.oi.leftStick.getZ());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.getRobotDrive().arcadeDrive(0,0);
    	Robot.driveTrain.extra.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
