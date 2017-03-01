package org.usfirst.frc.team2907.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PixyAuto extends CommandGroup
{
	public PixyAuto()
	{
		addSequential(new AlignTowerCommand(.25));
	}
}
