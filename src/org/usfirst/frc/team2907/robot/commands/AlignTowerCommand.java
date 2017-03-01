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
	
	static double kP = 0.08;
	static double kI = 0.00;
	static double kD = 0.00;
	static final double kToleranceDegrees = 1.0f;
	// pid controller for driving without drift and for turning
	private PIDController pidControllerTurn;
	private PIDOutputTurn outputTurn;
	
	public AlignTowerCommand(double power)
	{
		super("AlignTowerCommand");
		requires(Robot.driveTrain);
		requires(Robot.cameraManager);
		this.power = power;
	}
	
	protected void initialize()
	{
		
		//  tower x 232.0 
		// tower y 112.0 
		if (!Robot.cameraManager.isTowerInRange())
		{
			cancel();
			return;
		}
		// reset angle on nav board
		Robot.driveTrain.getSensorBoard().reset();
		// get p/i/d set on dashboard
		kP = Preferences.getInstance().getDouble("kP", .08);
		kI = Preferences.getInstance().getDouble("kI", 0);
		kD = Preferences.getInstance().getDouble("kD", 0);
		// init turn controller
		outputTurn = new PIDOutputTurn();
		pidControllerTurn = new PIDController(kP, kI, kD, Robot.driveTrain.getSensorBoard(), outputTurn);
		pidControllerTurn.setInputRange(-180, 180);
		pidControllerTurn.setOutputRange(-.5, .5); // turn fast, not driving straight at same time hopefully
		pidControllerTurn.setAbsoluteTolerance(kToleranceDegrees);
		pidControllerTurn.setContinuous(true);
		// get horizontal offset
		alignedHorizontal = true;
//		double horizontalOffset = Robot.cameraManager.getTowerXOffset();
//		if (horizontalOffset - HORIZONTAL_OFFSET > HORIZONTAL_ERROR)
//		{
//			// not on target, turn 90 and turn camera 90
//			pidControllerTurn.setSetpoint(90);
//			pidControllerTurn.enable();
//			alignedHorizontal = false;
//			Robot.cameraManager.turnSidewayTower();
//		}
	}
	
	protected void execute()
	{
		Robot.cameraManager.readCameras();
		if (pidControllerTurn.isEnabled() && pidControllerTurn.onTarget()) // done turning
		{
			pidControllerTurn.disable();
		} else if (pidControllerTurn.isEnabled()) // turning but not done
		{
			return;
		}
		
		if (!alignedHorizontal)
		{
			double horizontalOffset = Robot.cameraManager.getTowerXOffset();
			if (Math.abs(horizontalOffset - HORIZONTAL_OFFSET) < HORIZONTAL_ERROR)
			{
				Robot.driveTrain.arcadeDrive(0, 0);
				Robot.driveTrain.getSensorBoard().reset();
				pidControllerTurn.setSetpoint(-90);
				pidControllerTurn.enable();
				alignedHorizontal = true;
				Robot.cameraManager.turnStraightTower();
			} else
			{
				Robot.driveTrain.arcadeDrive((horizontalOffset - HORIZONTAL_OFFSET > 0) ? -power : power, 0);
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
				Robot.driveTrain.arcadeDrive((verticalOffset - VERTICAL_OFFSET > 0) ? power : -power, 0);
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
