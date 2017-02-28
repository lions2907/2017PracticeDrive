package org.usfirst.frc.team2907.robot.subsystems;

import org.usfirst.frc.team2907.robot.Robot;
import org.usfirst.frc.team2907.robot.RobotMap;
import org.usfirst.frc.team2907.robot.commands.ArcadeDrive;
import org.usfirst.frc.team2907.robot.commands.DelayedCallback;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {
	public static double DISTANCE_PER_FEET = 4.0*Math.PI; // Distance in inches, 4 inch wheels. 
	/* CANTALONS */
	private CANTalon left1 = new CANTalon(RobotMap.TALON_LEFT_1);
	private CANTalon left2 = new CANTalon(RobotMap.TALON_LEFT_2);
	private CANTalon left3 = new CANTalon(RobotMap.TALON_LEFT_3);
	private CANTalon right1 = new CANTalon(RobotMap.TALON_RIGHT_1);
	private CANTalon right2 = new CANTalon(RobotMap.TALON_RIGHT_2);
	private CANTalon right3 = new CANTalon(RobotMap.TALON_RIGHT_3);
	/* DRIVE ENCODERS */
	private Encoder driveEncoderLeft = new Encoder(0, 1);
	private Encoder driveEncoderRight = new Encoder(2, 3);
	/* SOLONOIDS FOR SHIFTER */
	private Solenoid leftSolenoid = new Solenoid(2);
	private Solenoid rightSolenoid = new Solenoid(3);
	private boolean isHighGear;
	/* NAVIGATION BOARD */
	private AHRS sensorBoard;
	private boolean navigationAvaliable;

	public DriveTrain() {
		// setup encoder
		driveEncoderLeft.setDistancePerPulse((1.0 / 100.0)); // 100 pulses per revolution and 2:1 gear ratio
		driveEncoderRight.setDistancePerPulse((1.0 / 100.0)); // 100 pulses per revolution and 2:1 gear ratio
		// setup talons
		right1.setInverted(true);
		right2.setInverted(true);
		right3.setInverted(true);
		try {
			// init sensor board
			sensorBoard = new AHRS(SPI.Port.kMXP);
			getSensorBoard().reset();
			navigationAvaliable = true;
		} catch (Exception e) {
			System.out.println("Error instantating sensorBoard : "
					+ e.getMessage());
		}
	}
	
	public void shift(boolean highGear) {
		isHighGear = highGear;
		if (highGear)
			leftSolenoid.set(true);
		else 
			rightSolenoid.set(true);
//		if (highGear) {
//			leftSolenoid.set(!highGear);
//			rightSolenoid.set(hsighGear);
//			isHighGear = true;
//		} else {
//			leftSolenoid.set(!highGear);
//			rightSolenoid.set(highGear);
//			isHighGear = false;
//		}
		Scheduler.getInstance().add(new DelayedCallback(0.25) {
			public void onCallback() {
				leftSolenoid.set(false);
				rightSolenoid.set(false);
//				shifter.set(DoubleSolenoid.Value.kOff);
			}
		});
	}

	public void arcadeDrive(double move, double rotate) {
		double leftSpeed = move + rotate;
		double rightSpeed = move - rotate;
		left1.set(leftSpeed);
		left2.set(leftSpeed);
		left3.set(leftSpeed);
		right1.set(rightSpeed);
		right2.set(rightSpeed);
		right3.set(rightSpeed);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDrive());
	}

	public boolean isHighGear() {
		return isHighGear;
	}

	public boolean isNavigationAvaliable() {
		return navigationAvaliable;
	}

	public AHRS getSensorBoard() {
		return sensorBoard;
	}
	
	public double getLeftDistance()
	{
		return driveEncoderLeft.getDistance();
	}
	
	public double getRightDistance()
	{
		return driveEncoderRight.getDistance();
	}
	
	public double getDistance()
	{
		return -(driveEncoderLeft.getDistance() + driveEncoderRight.getDistance()) / 2.0;
	}
	
	public void resetDistance()
	{
		driveEncoderLeft.reset();
		driveEncoderRight.reset();
	}
	
	public double getAngle()
	{
		return sensorBoard.getAngle();
	}
}
