package org.usfirst.frc.team2907.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem
{
	private Talon talon = new Talon(9);
	private DigitalInput limitSwitch ;
	
	public Shooter()
	{
		limitSwitch = new DigitalInput(1);
	}
	
	public boolean hasGear()
	{
		return limitSwitch.get();
	}
	
	public void shoot(double power)
	{
		System.out.println("limit switch status : " + hasGear());
		talon.set(power);
	}
	
	@Override
	protected void initDefaultCommand()
	{
	}

}
