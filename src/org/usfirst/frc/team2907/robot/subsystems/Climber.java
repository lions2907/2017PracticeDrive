package org.usfirst.frc.team2907.robot.subsystems;

import org.usfirst.frc.team2907.robot.Robot;
import org.usfirst.frc.team2907.robot.RobotMap;
import org.usfirst.frc.team2907.robot.commands.ClimberCommand;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {
	public CANTalon talon1 = new CANTalon(RobotMap.TALON_CLIMB_1);
	public CANTalon talon2 = new CANTalon(RobotMap.TALON_CLIMB_2);
	public Encoder climbEncoder = new Encoder(2, 3, false, EncodingType.k1X);
	
	public Climber()
	{
		climbEncoder.setDistancePerPulse(.33);
		climbEncoder.reset();
	}

	@Override
	protected void initDefaultCommand() {
		
		//setDefaultCommand(new ClimberCommand(Robot.oi.leftStick.getZ()));
		// TODO Auto-generated method stub
		
	}

}
