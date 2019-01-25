package org.usfirst.frc1735.DeepSpace2019.joysticks;

import edu.wpi.first.wpilibj.Joystick;

public abstract class AbstractJoystick {
    protected Joystick joystick;

    public AbstractJoystick(final Joystick joystick) {
        this.joystick = joystick;
        initializeKeymap();
    }

    abstract void initializeKeymap();

    public abstract double getX();

    public abstract double getY();
}