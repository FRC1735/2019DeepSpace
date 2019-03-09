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
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import org.usfirst.frc1735.DeepSpace2019.commands.UpdateClawFromSD;
import org.usfirst.frc1735.DeepSpace2019.commands.sensors.GP2Y0E02B_I2C_Rangefinder;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.I2C.Port;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Claw extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private VictorSP clawMotor;
    private DigitalInput ballSensor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    // Manually define our I2C-based optical rangefinder
    private GP2Y0E02B_I2C_Rangefinder rangefinder;

    public Claw() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        clawMotor = new VictorSP(0);
        addChild("ClawMotor",clawMotor);
        clawMotor.setInverted(false);
        
        ballSensor = new DigitalInput(0);
        addChild("BallSensor",ballSensor);
        
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    // Manually instantiate our rangefinder
        rangefinder = new GP2Y0E02B_I2C_Rangefinder(Port.kOnboard); // Use default device addr and default units
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        // We want the ball detector to light up (or change the color of) our onboard lights.
        if (isBallPresent()) {
            System.out.println("    Detected");
            //turn on the light.
            //FIXME how do we do this?
        }
        else System.out.println("NOT Detected");

    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void clawMove(double direction) {
        if (direction == this.out) {
            this.clawMotor.set(outMagDir);        
        }
        else if (direction == this.in) {
            this.clawMotor.set(inMagDir);
        }
        else {
            this.clawMotor.set(0); // stop
        }
    }

    // Update the claw intake/outake values from the Shuffleboard
    public void updateSetpointFromSD() {
        outMagDir = m_clawOutEntry.getDouble(0);
        inMagDir = m_clawInEntry.getDouble(0);
        //System.out.println("changed claw magnitudes to out= " + outMagDir + " in= " + inMagDir);
    }

    // Pointer to the Shuffleboard tab for the Arm:
    ShuffleboardTab m_clawTab;
    // Entries for each of the values we want to manipulate in the tab
    NetworkTableEntry m_clawOutEntry;
    NetworkTableEntry m_clawInEntry;

    public void initClaw() {
        m_clawTab = Shuffleboard.getTab("Claw"); // Creates tab if it doesn't already exist.

        m_clawOutEntry = m_clawTab.add("Claw out", kOutMagDir)
                                .withSize(2, 1) // make the widget 2 wide x1 tall
                                .withPosition(0, 0) // col, row
                                .getEntry();
        m_clawInEntry = m_clawTab.add("Claw In", kInMagDir)
                                .withSize(2, 1) // make the widget 2 wide x1 tall
                                .withPosition(2, 0) // col, row
                                .getEntry();
        // Command to trigger a read of all ARM-related variables from Shuffleboard
        m_clawTab.add("UpdateClawVals", new UpdateClawFromSD())
                                .withSize(4, 1)
                                .withPosition(4, 0);
    }

    // Function to use rangefinder to see if the ball is present (deprecated)
    public boolean isBallPresentRangefinderDeprecated() {
        // Get the current value from the rangefinder
        double distance = rangefinder.getRangeInches();
        // For debug purposes, let's continuously output the current rangefinder value
        SmartDashboard.putNumber("Range", distance);
        if (distance < kBallDetectDistanceInches) return true;
        else return false;
    }

public boolean isBallPresent() {
       // Function to use Beam Break Sensor to see if the ball is present
       // Wired so that signal = 1 if beam is intact; signal = 0 if beam is broken (ball present)
    return (!ballSensor.get());
}

    //variables for controlling direction
    public static final double stop=0;
    public static final double out=-1.0; // use this as an enum.  FIXME to be an actual enum later?
    public static final double in=1.0;

    public static final double kOutMagDir = -1.0; //compiled-in default for out
    public static final double kInMagDir =  0.6; //compiled-in default for in
    public static double outMagDir = kOutMagDir; // Start with the compiled value; can override later
    public static double inMagDir = kInMagDir; // Start with the compiled value; can override later

    public static final double kBallDetectDistanceInches = 18; //Any range reported smaller than this indicates a ball is loaded.

}

