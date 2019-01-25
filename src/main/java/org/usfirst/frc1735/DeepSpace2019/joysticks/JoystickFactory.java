package org.usfirst.frc1735.DeepSpace2019.joysticks;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class JoystickFactory {
    public static AbstractJoystick get(final Joystick joystick) {
        //DriverStation.getInstance().getJoystickIsXbox(stick)
        return new XBoxJoystick(joystick);
    }
}