package org.usfirst.frc.team2907.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AlignPixyAuto extends CommandGroup
{
	public AlignPixyAuto()
	{
		addSequential(new AlignPixyCommand());
	}
}
