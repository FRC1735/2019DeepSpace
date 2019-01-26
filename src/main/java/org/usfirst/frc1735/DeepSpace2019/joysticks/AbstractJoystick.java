package org.usfirst.frc1735.DeepSpace2019.joysticks;

import edu.wpi.first.wpilibj.Joystick;

public abstract class AbstractJoystick {
    protected Joystick joystick;
    protected Role role;

    public AbstractJoystick(final Joystick joystick, final Role role) {
        this.joystick = joystick;
        this.role = role;
        initializeKeymap();
    }

    abstract void initializeKeymap();

    public abstract double getX();

    public abstract double getY();

    public abstract double getXSwivelStick();

    public abstract double getYSwivelStick();

    public abstract boolean isCapableOfSoloTankMode();
}