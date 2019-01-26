package org.usfirst.frc1735.DeepSpace2019.joysticks;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class JoystickFactory {
    public static AbstractJoystick get(final Joystick joystick, final Role role) {
        if (DriverStation.getInstance().getJoystickIsXbox(joystick.getPort())) {
            return new XBoxJoystick(joystick, role);
        } else {
            return new Attack3Joystick(joystick, role);
        }
    }
}