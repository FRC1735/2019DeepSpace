// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc1735.DeepSpace2019.subsystems;

import org.usfirst.frc1735.DeepSpace2019.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;

/**
 *
 */
public class AlienDeployer extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private CANSparkMax alienDeployerMotor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private CANDigitalInput m_forwardLimit;
    private CANDigitalInput m_reverseLimit;

    
    public AlienDeployer() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        alienDeployerMotor = new CANSparkMax(7, MotorType.kBrushed);
        
        
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        alienDeployerMotor.setInverted(true);
        m_forwardLimit = alienDeployerMotor.getForwardLimitSwitch(LimitSwitchPolarity.kNormallyOpen);
        m_reverseLimit = alienDeployerMotor.getReverseLimitSwitch(LimitSwitchPolarity.kNormallyOpen);
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        //setDefaultCommand(new AlienDeployWithJoystick());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    public State getState() {
        if (isReverseLimitPressed()) {
            return State.RETRACTED;
        } else if (isForwardLimitPressed()) {
            return State.EXTENDED;
        } else {
            return State.IN_MOTION;
        }
    }

    public boolean isForwardLimitPressed() {
        return m_forwardLimit.get();
    }

    public boolean isReverseLimitPressed() {
        return m_reverseLimit.get();
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void alienDeployerMove(double magDir) {
        System.out.println("alienDeployerMove with magDir= " + magDir);
        alienDeployerMotor.set(magDir);
        if (m_forwardLimit.get()) {
            System.out.println("forward limit hit");
        }
        if (m_reverseLimit.get()) {
            System.out.println("reverse limit hit");
        }

    }

    public enum State {
        EXTENDED, RETRACTED, IN_MOTION
    }

    // variables for controlling direction
    public static final double stop = 0;
    public static final double out = 1.0;
    public static final double in = -1.0;

    // TODO - these may need to be adjusted
    static final double kExtendedPosition = 1;
    static final double kRetractedPosition = 0;
}
