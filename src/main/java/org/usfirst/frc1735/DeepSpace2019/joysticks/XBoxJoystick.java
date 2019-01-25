package org.usfirst.frc1735.DeepSpace2019.joysticks;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterArcadeMode;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterTankMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XBoxJoystick extends AbstractJoystick {
    public XBoxJoystick(final Joystick joystick) {
        super(joystick);
    }

    @Override
    public double getX() {
        return joystick.getRawAxis(4);
    }

    @Override
    public double getY() {
        return joystick.getRawAxis(5);
    }

    @Override
    void initializeKeymap() {
        // X Button
        Robot.oi.arcadeMode = new JoystickButton(joystick, 3);
        Robot.oi.arcadeMode.whenPressed(new EnterArcadeMode());

        // Y Button
        Robot.oi.tankMode = new JoystickButton(joystick, 4);
        Robot.oi.tankMode.whenPressed(new EnterTankMode());
	}

}
