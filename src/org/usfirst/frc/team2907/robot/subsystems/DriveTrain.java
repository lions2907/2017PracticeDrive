package org.usfirst.frc.team2907.robot.subsystems;

import org.usfirst.frc.team2907.robot.Robot;
import org.usfirst.frc.team2907.robot.RobotMap;
import org.usfirst.frc.team2907.robot.commands.ArcadeDrive;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem 
{
	private CANTalon left1 = new CANTalon(RobotMap.TALON_LEFT_1);
	private CANTalon left2 = new CANTalon(RobotMap.TALON_LEFT_2);
	private CANTalon right1 = new CANTalon(RobotMap.TALON_RIGHT_1);
	private CANTalon right2 = new CANTalon(RobotMap.TALON_RIGHT_2);
	public CANTalon extra = new CANTalon(RobotMap.TALON_EXTRA);
	private Solenoid highSolenoid = new Solenoid(RobotMap.SOLENOID_1);
	private Solenoid lowSolenoid2 = new Solenoid(RobotMap.SOLENOID_2);
	
	private RobotDrive robotDrive;
	public boolean status = false;
	
	public DriveTrain()
	{
		robotDrive = new RobotDrive(left1, left2, right1, right2);
	}
	
	public RobotDrive getRobotDrive()
	{
		return robotDrive;
	}
	
	public void shift(boolean up)
	{
		highSolenoid.set(up);
		lowSolenoid2.set(!up);
		status = up;
	}

    public void initDefaultCommand() {
    	setDefaultCommand(new ArcadeDrive());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

