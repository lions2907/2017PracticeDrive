package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTillColisionCommand extends Command
{
	double last_world_linear_accel_x;
	double last_world_linear_accel_y;
	final static double kCollisionThreshold_DeltaG = 0.5f;


	public DriveTillColisionCommand()
	{
		super("Drive untill Colision");
		requires(Robot.driveTrain);
	}

	@Override
	protected boolean isFinished()
	{
		return false;
	}

	// Called just before this Command runs the first time
	protected void initialize()
	{
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute()
	{
		Robot.driveTrain.getRobotDrive().arcadeDrive(-Robot.oi.leftStick.getY(), Robot.oi.leftStick.getZ());
		boolean collisionDetected = false;
		
		double curr_world_linear_accel_x = Robot.driveTrain.sensorBoard.getWorldLinearAccelX();
        double currentJerkX = curr_world_linear_accel_x - last_world_linear_accel_x;
        double last_world_linear_accel_x = curr_world_linear_accel_x;
        double curr_world_linear_accel_y = Robot.driveTrain.sensorBoard.getWorldLinearAccelY();
        double currentJerkY = curr_world_linear_accel_y - last_world_linear_accel_y;
        last_world_linear_accel_y = curr_world_linear_accel_y;
        
        if ( ( Math.abs(currentJerkX) > kCollisionThreshold_DeltaG ) ||
             ( Math.abs(currentJerkY) > kCollisionThreshold_DeltaG) ) {
            collisionDetected = true;
        }
        System.out.println("collision detected : " + collisionDetected + " currentJerkX : " + currentJerkX + " currentJerkY : " + currentJerkY );
	}

	// Called once after isFinished returns true
	protected void end()
	{
		Robot.driveTrain.getRobotDrive().arcadeDrive(0,0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted()
	{
		end();
	}
	
}
