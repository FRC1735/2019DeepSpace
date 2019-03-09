package org.usfirst.frc1735.DeepSpace2019.joysticks;


import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.AlienDeploy;
import org.usfirst.frc1735.DeepSpace2019.commands.ClawCmd;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterArcadeMode;
import org.usfirst.frc1735.DeepSpace2019.commands.EnterTankMode;
import org.usfirst.frc1735.DeepSpace2019.commands.HatchManipulator;
import org.usfirst.frc1735.DeepSpace2019.commands.MoveArmDegrees;
import org.usfirst.frc1735.DeepSpace2019.commands.TareArm;
import org.usfirst.frc1735.DeepSpace2019.subsystems.AlienDeployer;
import org.usfirst.frc1735.DeepSpace2019.subsystems.Arm;
import org.usfirst.frc1735.DeepSpace2019.subsystems.Claw;
import org.usfirst.frc1735.DeepSpace2019.subsystems.HatchGrabber;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Attack3Joystick extends AbstractJoystick {

    public Attack3Joystick(final Joystick joystick, final Role role) {
        super(joystick, role);
    }

    @Override
    public double getX() {
        return joystick.getX();
    }

    @Override
    public double getY() {
        return joystick.getY();
    }
    
    @Override
    void initializeKeymap() {
        switch (role) {
            case DRIVER_LEFT:
                Robot.oi.arcadeMode = new JoystickButton(joystick, 9);
                Robot.oi.arcadeMode.whenPressed(new EnterArcadeMode());
                
                Robot.oi.tankMode = new JoystickButton(joystick, 10);
                Robot.oi.tankMode.whenPressed(new EnterTankMode());

                Robot.oi.clawIn = new JoystickButton(joystick, 2);
                // WhileHeld seems to cause stuttering (perhaps new behavior in 2019?  It starts and stops and stutters.  perhaps terminating the command and stopping the motor every iteration?)
                // Workaround:  Explicit commands on press and release to accomplish the same thing
                Robot.oi.clawIn.whenPressed(new ClawCmd(Claw.in));
                Robot.oi.clawIn.whenReleased(new ClawCmd(0));
                
                Robot.oi.clawOut = new JoystickButton(joystick, 1);
                Robot.oi.clawOut.whenPressed(new ClawCmd(Claw.out));
                Robot.oi.clawOut.whenReleased(new ClawCmd(0));
                break;  
           
            case DRIVER_RIGHT:
                break;

            case OPERATOR:
                Robot.oi.releaseHatch = new JoystickButton(joystick, 4);
                Robot.oi.releaseHatch.whenPressed(new HatchManipulator(HatchGrabber.out));
                
                Robot.oi.grabHatch = new JoystickButton(joystick, 5);
                Robot.oi.grabHatch.whenPressed(new HatchManipulator(HatchGrabber.in));
                
                Robot.oi.alienAttack = new JoystickButton(joystick, 3);
                Robot.oi.alienAttack.whenPressed(new AlienDeploy(AlienDeployer.out));
                
                Robot.oi.alienRetreat = new JoystickButton(joystick, 2);
                Robot.oi.alienRetreat.whenPressed(new AlienDeploy(AlienDeployer.in));
                
                Robot.oi.armPresetForwardBallPickUp = new JoystickButton(joystick, 11);
                Robot.oi.armPresetForwardBallPickUp.whenPressed(new MoveArmDegrees(Arm.kForwardBallPickup));

                Robot.oi.armPresetForwardHatchPickUp = new JoystickButton(joystick, 10);
                Robot.oi.armPresetForwardHatchPickUp.whenPressed(new MoveArmDegrees(Arm.kForwardHatchPickup));

                Robot.oi.armPresetForwardRocketOneDrop = new JoystickButton(joystick, 9);
                Robot.oi.armPresetForwardRocketOneDrop.whenPressed(new MoveArmDegrees(Arm.kForwardRocketOne));

                Robot.oi.armPresetUp = new JoystickButton(joystick, 1);
                Robot.oi.armPresetUp.whenPressed(new MoveArmDegrees(Arm.kUp));
                
                Robot.oi.armPresetBackwardRocketOneDrop = new JoystickButton(joystick, 8);
                Robot.oi.armPresetBackwardRocketOneDrop.whenPressed(new MoveArmDegrees(Arm.kBackwardRocketOne));

                Robot.oi.armPresetBackwardHatchPickUp = new JoystickButton(joystick, 7);
                Robot.oi.armPresetBackwardHatchPickUp.whenPressed(new MoveArmDegrees(Arm.kBackwardHatchPickup));

                Robot.oi.armPresetBackwardBallPickUp = new JoystickButton(joystick, 6);
                Robot.oi.armPresetBackwardBallPickUp.whenPressed(new MoveArmDegrees(Arm.kBackwardBallPickup));
                break;
        }
    }

    @Override
    public boolean isCapableOfSoloTankMode() {
        return false;
    }

    @Override
    public double getSwivelStickX() {
        return 0;
    }

    @Override
    public double getSwivelStickY() {
        return 0;
    }
}