package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 *
 */
public class AlignTowerCommand extends Command
{
	// constants for easy changes
	public static final int HORIZONTAL_OFFSET = 180;
	public static final int VERTICAL_OFFSET = 80;
	public static final int HORIZONTAL_ERROR = 10;
	public static final int VERTICAL_ERROR = 10;
	
	private boolean alignedHorizontal;
	private boolean alignedVertical;
	
	private double power;
	
	public AlignTowerCommand(double power)
	{
		super("AlignTowerCommand");
		requires(Robot.driveTrain);
		requires(Robot.cameraManager);
		this.power = power;
	}
	
	protected void initialize()
	{
		alignedHorizontal = false;
		alignedHorizontal = false;
		//  tower x 232.0 
		// tower y 112.0 
		if (!Robot.cameraManager.isTowerInRange())
		{
			cancel();
			return;
		}
		
		double horizontalOffset = Robot.cameraManager.getTowerXOffset();
		if (horizontalOffset - HORIZONTAL_OFFSET > HORIZONTAL_ERROR)
		{
			// not on target, turn 90 and turn camera 90
			alignedHorizontal = false;
			Robot.cameraManager.turnSidewayTower();
		}
	}
	
	protected void execute()
	{
		Robot.cameraManager.readCameras();
		
		if (!alignedHorizontal)
		{
			double horizontalOffset = Robot.cameraManager.getTowerXOffset();
			if (Math.abs(horizontalOffset - HORIZONTAL_OFFSET) < HORIZONTAL_ERROR)
			{
				Robot.driveTrain.arcadeDrive(0, 0);
				alignedHorizontal = true;
			} else
			{
				Robot.driveTrain.arcadeDrive(0, (horizontalOffset - HORIZONTAL_OFFSET > 0) ? -power : power);
			}
		} else if (alignedHorizontal && !alignedVertical)
		{
			double verticalOffset = Robot.cameraManager.getTowerYOffset();
			if (Math.abs(verticalOffset - VERTICAL_OFFSET) < VERTICAL_ERROR)
			{
				alignedVertical = true;
				Robot.driveTrain.arcadeDrive(0, 0);
			} else 
			{
				Robot.driveTrain.arcadeDrive((verticalOffset - VERTICAL_OFFSET > 0) ? -power : power, 0);
			}
		}
	}
	
	protected boolean isFinished()
	{
		if (alignedHorizontal && alignedVertical)
		{
			System.out.println("We did it!");
		}
		return alignedHorizontal && alignedVertical;
	}
	
	protected void end()
	{
	}
	
	protected void interrupted()
	{
		end();
	}
	
	class PIDOutputTurn implements edu.wpi.first.wpilibj.PIDOutput
	{
		
		public void pidWrite(double output)
		{
			Robot.driveTrain.arcadeDrive(0, -output);
		}
		
	}
}
