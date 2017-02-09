package org.usfirst.frc.team2907.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveStraightAuto extends CommandGroup
{
	public DriveStraightAuto()
	{
		addSequential(new TimeDriveCommand(10));
	}
}
