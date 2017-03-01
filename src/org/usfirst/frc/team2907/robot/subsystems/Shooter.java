package org.usfirst.frc.team2907.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem
{
	private Talon talon = new Talon(0);
	
	public void shoot(double power)
	{
		talon.set(power);
	}
	
	@Override
	protected void initDefaultCommand()
	{
	}

}
