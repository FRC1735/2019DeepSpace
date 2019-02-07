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


// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.commands.DriveWithJoystick;
import org.usfirst.frc1735.DeepSpace2019.smartdashboard.SmartDashboardKeys;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
    

/**
 *
 */
public class DriveTrain extends Subsystem implements PIDOutput {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX leftMotor;
    private WPI_TalonSRX rightMotor;
    private DifferentialDrive differentialDrive1;
    private WPI_VictorSPX leftFollower;
    private WPI_VictorSPX rightFollower;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public DriveTrain() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        leftMotor = new WPI_TalonSRX(1);
        
        
        
        rightMotor = new WPI_TalonSRX(2);
        
        
        
        differentialDrive1 = new DifferentialDrive(leftMotor, rightMotor);
        addChild("Differential Drive 1",differentialDrive1);
        differentialDrive1.setSafetyEnabled(true);
        differentialDrive1.setExpiration(0.1);
        differentialDrive1.setMaxOutput(1.0);

        
        leftFollower = new WPI_VictorSPX(3);
        
        
        
        rightFollower = new WPI_VictorSPX(4);
        
        
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

    leftFollower.follow(leftMotor);
    rightFollower.follow(rightMotor);
    leftFollower.setSafetyEnabled(false);
    rightFollower.setSafetyEnabled(false);

    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new DriveWithJoystick());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putNumber("Yaw", Robot.ahrs.getYaw()); //-180 to +180 from last zeroed yaw setting

    }

    // Getter functions for our motors
    public WPI_TalonSRX getLeftMotor() {
        return leftMotor;
    }
    public WPI_TalonSRX getRightMotor() {
        return rightMotor;
    }
    
    public void drivetrainInit() {
    	// Do initialization that cannot be done in the constructor because robot.init isn't executed yet so we don't have a gyro instance.
    	
    	//-----------------------------
    	// Software PID Subsystem Initialization
    	//-----------------------------
    	//PID values.values  F is feed-forward for maintaining rotational velocity.
    	// The last two args are input source and output object (our PIDwrite() function)
        drivelineController = new PIDController(kLargeTurnP, kLargeTurnI, kLargeTurnD, kLargeTurnF, Robot.ahrs, this);
        drivelineController.setInputRange(-180.0f, 180.0f); // WHY IS THIS -180 to +180 RATHER THAN 0-360????
        drivelineController.setOutputRange(-0.75, 0.75); // What is the allowable range of values to send to the output (our motor rotation)
        drivelineController.setAbsoluteTolerance(kToleranceDegrees); // How close do we have to be in order to say we have reached the target?
        // Robot can spin in full circle so angle might wrap from 0 to 360.
        // 'Continuous' allows us to follow that wrap to get from 359 to 0, rather than going counterclockwise a full circle to get there.
        drivelineController.setContinuous(true);        
        /* Add the PID Controller to the Test-mode dashboard, allowing manual  */
        /* tuning of the Turn Controller's P, I and D coefficients.            */
        /* Typically, only the P value needs to be modified.                   */
        LiveWindow.addActuator("DriveSystem", "RotateController", drivelineController);
            	
    	//-----------------------------
    	//Hook up to the Limelight
    	//-----------------------------
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tv = table.getEntry("tv"); // valid bit (target is detected)        
        tx = table.getEntry("tx"); // Horizontal angle to target in degrees
        ty = table.getEntry("ty"); // vertical angle to target in degrees
        ta = table.getEntry("ta"); // Area of target relative to full screen image in percent
        tx0 = table.getEntry("tx0"); // one of three raw Tx values.
        tx1 = table.getEntry("tx1"); // one of three raw Tx values.
        tx2 = table.getEntry("tx2"); // one of three raw Tx values.
        
    	//-----------------------------
        // Talon open- and closed-loop configuration
        //-----------------------------

        // Chose the sensor and direction
    	leftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0); // extra args are:  primary closed loop, timeout in ms
    	leftMotor.setSensorPhase(true); //Assume inversion
    	rightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    	rightMotor.setSensorPhase(true); //Assume inversion.
    	// Followers do not have sensors.
    	
    	// Voltage compensation mode should make 100% output request scale to 12V regardless of battery voltage.
    	// (if battery voltage is less than 12v it will just put all available voltage out)
    	leftMotor.enableVoltageCompensation(true);
    	rightMotor.enableVoltageCompensation(true);
    	
    	// Set the desired 100% at 12V
    	leftMotor.configVoltageCompSaturation(12.0, 0);
    	rightMotor.configVoltageCompSaturation(12.0, 0);
    	
    	// Encoder setup:  Since we are using the CTRE Mag Encoders, we do not
    	// need to configure anything further
    	
    	// Closed-loop PID initialization/configuration
    	//
        // See full code example at https://github.com/CrossTheRoadElec/FRC-Examples-STEAMWORKS/blob/master/JAVA_MotionMagicExample/src/org/usfirst/frc/team217/robot/Robot.java
        // General documentation on CTRE examples at https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/tree/master/Java

    	// set the peak and nominal outputs, -1 to 1 in percentage of nominal voltage (even if battery voltage is higher)
    	leftMotor.configNominalOutputForward(0.0, 0);
    	leftMotor.configNominalOutputReverse(-0.0, 0);
    	leftMotor.configPeakOutputForward(1.0,0);
    	leftMotor.configPeakOutputReverse(-1.0,0);
    	rightMotor.configNominalOutputForward(0.0, 0);
    	rightMotor.configNominalOutputReverse(-0.0, 0);
    	rightMotor.configPeakOutputForward(1.0,0);
    	rightMotor.configPeakOutputReverse(-1.0,0);

    	// set closed loop gains in slot0 - see documentation
    	leftMotor.selectProfileSlot(0,0);
    	rightMotor.selectProfileSlot(0,0);

    	// set acceleration and vcruise velocity - see documentation
    	leftMotor.configMotionCruiseVelocity(2700, 0); // 2700 encoder units per 100ms interval is about 395 RPM
    	rightMotor.configMotionCruiseVelocity(2700, 0);

    	// It's not clear how the MotionAcceleration and the ClosedloopRamp differ...
    	leftMotor.configMotionAcceleration(kAccelUnits, 0); //want to get to full speed in 1/3 sec, so triple the velocity
    	rightMotor.configMotionAcceleration(kAccelUnits, 0); 

    	// Set the closed loop ramp as well (time in seconds to full speed; timeout)
    	leftMotor.configClosedloopRamp(kAccelTime, 0); //want to get to full speed in 1/3 sec
    	leftMotor.configOpenloopRamp(kAccelTime, 0);
    	rightMotor.configClosedloopRamp(kAccelTime, 0);
    	rightMotor.configOpenloopRamp(kAccelTime, 0);


    	//Set the closed-loop allowable error.  Empirically on no-load, error was <50 units.
    	leftMotor.configAllowableClosedloopError(0, (int) kToleranceDistUnits, 0); // index, err, timeout in ms
    	rightMotor.configAllowableClosedloopError(0, (int) kToleranceDistUnits, 0); // index, err, timeout in ms
    	
    	// Some packet frames default to updating at 160ms, which is waaaay too slow for our 20ms DS periodic interval!
		// CTRE recommends that you set relevant frame periods to be at least as fast as periodic rate-- their example uses:
		leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 0);
		leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 0);
		rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 0);
		rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 0);


    	// Throw a lot of settings up on the SmartDashboard...
    	SmartDashboard.putNumber("Cruise SpeedDir", 2700); // speed in units per 100ms (2745 is full speed)
    	SmartDashboard.putNumber("Cruise Dist", 72); // inches (DriveWithPID will convert to encoder ticks)
    	SmartDashboard.putNumber("Cruise Accel", kAccelUnits); //8100 = 1/3 sec to get to full 2700 speed
    	SmartDashboard.putNumber("Cruise R Accel", kAccelUnits);     	//10000 = Temporary to allow setting left vs right accel separately
    	SmartDashboard.putNumber("P", kDistP);
    	SmartDashboard.putNumber("I", kDistI);
    	SmartDashboard.putNumber("D", kDistD);
    	SmartDashboard.putNumber("F", kDistF);
        
        // Default to be in "drive by joystick" mode
        // if we are going into Autonomous, the first Command will properly set the operating mode so we're still ok
        setOpenLoopMode();

    }
       
    public void setOpenLoopMode() {
    	// This function does the Talon configuration changes to allow operation in an open-loop mode
    	// (Which is what we use for joystick control)
    	
        // in open loop mode, the WPI lib handles motor controller inversion, so turn it off in the hardware.
        rightFollower.setInverted(false);
        
    	// Set our mode to be the open-loop drive-by-joystick behavior:
    	leftMotor.set(ControlMode.PercentOutput, 0); // Mode, setpoint
    	rightMotor.set(ControlMode.PercentOutput, 0); // Mode, setpoint

    	// Put the Talons and Victors in "Coast" mode
    	leftMotor.setNeutralMode(NeutralMode.Coast);
    	rightMotor.setNeutralMode(NeutralMode.Coast);
        leftFollower.setNeutralMode(NeutralMode.Coast);
        rightFollower.setNeutralMode(NeutralMode.Coast);
        
        // When driving in Open Loop mode via the differentialDrive object, turn on the WPILib safety checks
        differentialDrive1.setSafetyEnabled(false);

    }

    public void setPIDMode() {
    	// This function does the Talon configuration changes to allow operation in a closed-loop PID mode.
    	// this allows us to use the Talon HW PID for positional control,
    	// and if in MotionMagic mode, it will also obey the trapezoidal motion profile for acceleration
    	// and maximum velocity.

    	// In closed-loop mode we talk to the motor controllers directly, and don't get the inversion of the right-hand motors that's implemented
    	// in the WPIlib DifferentialDrive class.  So, we have to invert the motor controller hardware directly:
    	rightMotor.setInverted(true);
        rightFollower.setInverted(true);
    	
       	//Set the mode to Magic (for the master; slaves match for now).  Second arg is the setpoint.
        leftMotor.set(ControlMode.MotionMagic, 0);
        rightMotor.set(ControlMode.MotionMagic, 0);
   	
    	// Put the Talons in "Brake" mode for better accuracy
    	leftMotor.setNeutralMode(NeutralMode.Brake);
    	rightMotor.setNeutralMode(NeutralMode.Brake);
        leftFollower.setNeutralMode(NeutralMode.Brake);
        rightFollower.setNeutralMode(NeutralMode.Brake);
 
    	// Zero out the relative sensors
        leftMotor.setSelectedSensorPosition(0,0,0); // position, PIDidx (0= normal), timeout in ms
        rightMotor.setSelectedSensorPosition(0,0,0); // position, PIDidx (0= normal), timeout in ms
   	
        // When driving in PID mode, turn off the WPILib safety checks
        differentialDrive1.setSafetyEnabled(true);
    }

    public void drive(final double joystickAX, final double joystickAY, final double joystickBX, final double joystickBY) {
        final String driveMode = SmartDashboard.getString(SmartDashboardKeys.DRIVETRAIN_MODE, "ARCADE");

        if (driveMode.equals("TANK")) {
            tankDrive(joystickAY, joystickBY);
        } else { // ARCADE is the default/fallback
            arcadeDrive(joystickAX, joystickAY);
        }
    }

    // Autonomous Turn command wants to control the driveline directly in tank mode, so expose as public
    public void tankDrive(final double joystickAY, final double joystickBY) {
        differentialDrive1.tankDrive(joystickAY, joystickBY);
    }

    private void arcadeDrive(final double joystickX, final double joystickY) {
        differentialDrive1.arcadeDrive(joystickY, joystickX, true);
    }

    public void stop() {
        differentialDrive1.stopMotor();
    }

    public void setArcadeMode() {
        SmartDashboard.putString(SmartDashboardKeys.DRIVETRAIN_MODE, "ARCADE");    
    }

    public void setTankMode(){
        SmartDashboard.putString(SmartDashboardKeys.DRIVETRAIN_MODE, "TANK");
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    @Override
    /* This function is invoked periodically by the PID Controller, */
    /* based upon navX MXP yaw angle input and PID Coefficients.    */
    public void pidWrite(double output) {
        m_rotateToAngleRate = output;
    }


    //Expose thes initial compiled-in settings to the SmartDashboard for development work and overrides.  We will only sample them once, when enabling the robot.
    // SOME of these values can be different between practice and competition robots, so take that into account for initial settings...
    public static void updatePIDValuesToSD() {
        // get the current value of PracticeBot from the Smart Dashboard
        boolean isPracticeBot = Robot.getPracticeBotFromSD();
    	SmartDashboard.putBoolean("TurnAbsolute", true); // Assume absolute mode
    	SmartDashboard.putNumber("TurnAngle", 0); // If relative, zero means no change.
        SmartDashboard.putNumber("TurnLP", isPracticeBot?kPracticeLargeTurnP:kLargeTurnP);
    	SmartDashboard.putNumber("TurnLI", kLargeTurnI);
    	SmartDashboard.putNumber("TurnLD", kLargeTurnD);
    	SmartDashboard.putNumber("TurnLMax", isPracticeBot?kPracticeLargeTurnPIDOutputMax:kLargeTurnPIDOutputMax);
    	SmartDashboard.putNumber("TurnMP", isPracticeBot?kPracticeMedTurnP:kMedTurnP);
    	SmartDashboard.putNumber("TurnMI", kMedTurnI);
    	SmartDashboard.putNumber("TurnMD", kMedTurnD);
    	SmartDashboard.putNumber("TurnMMax", isPracticeBot?kPracticeMedTurnPIDOutputMax:kMedTurnPIDOutputMax);
    	SmartDashboard.putNumber("TurnSP", isPracticeBot?kPracticeSmallTurnP:kSmallTurnP);
    	SmartDashboard.putNumber("TurnSI", kSmallTurnI);
    	SmartDashboard.putNumber("TurnSD", kSmallTurnD);
    	SmartDashboard.putNumber("TurnSMax", isPracticeBot?kPracticeSmallTurnPIDOutputMax:kSmallTurnPIDOutputMax);
    	
        SmartDashboard.putNumber("TurnErr", kToleranceDegrees);
}
    // This function samples the PID values from the SmartDashboard and stores them locally.
    //This allows us to change the values from the SmartDashboard, but store them locally for higher performance.
    // Use the current value as the default if SD is broken and doesn't return a value...
    public static void updatePIDValuesFromSD() {
        smallTurnP = SmartDashboard.getNumber("TurnSP", smallTurnP);
        smallTurnI = SmartDashboard.getNumber("TurnSI", smallTurnI);
        smallTurnD = SmartDashboard.getNumber("TurnSD", smallTurnD);
        smallTurnPIDOutputMax = SmartDashboard.getNumber("TurnSMax", smallTurnPIDOutputMax);
 
        medTurnP = SmartDashboard.getNumber("TurnMP", medTurnP);
        medTurnI = SmartDashboard.getNumber("TurnMI", medTurnI);
        medTurnD = SmartDashboard.getNumber("TurnMD", medTurnD);
        medTurnPIDOutputMax = SmartDashboard.getNumber("TurnMMax", medTurnPIDOutputMax);

        largeTurnP = SmartDashboard.getNumber("TurnLP", largeTurnP);
        largeTurnI = SmartDashboard.getNumber("TurnLI", largeTurnI);
        largeTurnD = SmartDashboard.getNumber("TurnLD", largeTurnD);
        largeTurnPIDOutputMax = SmartDashboard.getNumber("TurnLMax", largeTurnPIDOutputMax);
    }

    public void setSmallTurnPID() {
        drivelineController.setPID(smallTurnP,smallTurnI,smallTurnD, smallTurnF);
        drivelineController.setOutputRange(-smallTurnPIDOutputMax, smallTurnPIDOutputMax); // What is the allowable range of values to send to the output (our motor rotation)
    }
    
    public void setMedTurnPID() {
        drivelineController.setPID(medTurnP,medTurnI,medTurnD, medTurnF);
        drivelineController.setOutputRange(-medTurnPIDOutputMax, medTurnPIDOutputMax); // What is the allowable range of values to send to the output (our motor rotation)
    }
 
    public void setLargeTurnPID() {
            double p = SmartDashboard.getNumber("TurnLP", kLargeTurnP);
            double i = SmartDashboard.getNumber("TurnLI", kLargeTurnI);
            double d = SmartDashboard.getNumber("TurnLD", kLargeTurnD);
            double max = SmartDashboard.getNumber("TurnLMax", kLargeTurnPIDOutputMax);
 
        drivelineController.setPID(largeTurnP,largeTurnI,largeTurnD, largeTurnF);
        drivelineController.setOutputRange(-largeTurnPIDOutputMax, largeTurnPIDOutputMax); // What is the allowable range of values to send to the output (our motor rotation)
    }
 
    public double getTv() {
        return tv.getDouble(0); // Valid bit
    }
     
     public double getTx() {
         return tx.getDouble(0);
     }
     public double getTx0() {
         return tx0.getDouble(0);
     }
     public double getTx1() {
         return tx1.getDouble(0);
     }
     public double getTx2() {
         return tx2.getDouble(0);
     }
 
     public double getTy() {
         return ty.getDouble(0);
     }
 
 
     // Member Variables
     public static int kAbsolute = 0; //functions can use this as a parameter to the Turn command
     public static int kRelative = 1; //functions can use this as a parameter to the Turn command
     public static int kCamera = 2; //functions can use this as a parameter to the Turn command
     public static boolean kUseCamera = true; // used in DriveWithPID to override distance:  Instead, distance is calculated via the camera.
 
     //--------------------------------
     // SW PID subsystem variables
     //--------------------------------
     public PIDController drivelineController; // Use this PID controller to accomplish turns and track straight when driving forward.
     public double m_rotateToAngleRate; // PID output tells us how much to rotate to reach the desired setpoint target.
     
     //Limelight connections
     NetworkTable table;
     NetworkTableEntry tv;
     NetworkTableEntry tx, tx0, tx1, tx2;
     NetworkTableEntry ty;
     NetworkTableEntry ta;
 
     /* The following Software PID Controller coefficients will need to be tuned */
     /* to match the dynamics of your drive system.  Note that the      */
     /* SmartDashboard in Test mode has support for helping you tune    */
     /* controllers by displaying a form where you can enter new P, I,  */
     /* and D constants and test the mechanism.                         */
     
     public static final double kEncoderTicksPerInch = (4096 / (3.1415 * 6)); // 4096 encoder ticks per revolution; wheel diameter is nominally 6"
     static final double kToleranceDegrees = 0.5; // Stop if we are within this many degrees of the setpoint.
     public static final double kToleranceDistUnits = (int) 1/*inches*/ * kEncoderTicksPerInch; // stop if we are within this many encoder units of our setpoint.  18.85 inches/rev and 4096 ticks/rev means .25" is ~50 encoder ticks
 
     static final double kLargeTurnP = 0.015;
     static final double kPracticeLargeTurnP = 0.015;
     static final double kLargeTurnI = 0.00; //0.001504;
     static final double kLargeTurnD = 0.00;
     static final double kLargeTurnF = 0.00;
     public static final double kLargeTurnPIDOutputMax = 0.55; // Max motor output in large PID mode 0.55
     public static final double kPracticeLargeTurnPIDOutputMax = 0.55; // Max motor output in large PID mode 0.55

 
     public static final double kMedTurnPIDLimit = 15.0; // Errors smaller than this number of degrees should use the medium PID profile
     static final double kMedTurnP = 0.2; //use a very large (relative to normal) P to guarantee max motor output
     static final double kPracticeMedTurnP = 0.1; //use a very large (relative to normal) P to guarantee max motor output
     static final double kMedTurnI = 0.00;
     static final double kMedTurnD = 0.5;
     static final double kMedTurnF = 0.00;
     public static final double kMedTurnPIDOutputMax = 0.24; // Max motor output in medium PID mode
     public static final double kPracticeMedTurnPIDOutputMax = 0.24; // Max motor output in medium PID mode
 
     public static final double kSmallTurnPIDLimit = 1; // Errors smaller than this number of degrees should use the small PID profile
     static final double kSmallTurnP = 0.4; //use a very large (relative to normal) P to guarantee max motor output
     static final double kPracticeSmallTurnP = 0.2; //use a very large (relative to normal) P to guarantee max motor output
     static final double kSmallTurnI = 0.1;
     static final double kSmallTurnD = 0.5;
     static final double kSmallTurnF = 0.00;
     public static final double kSmallTurnPIDOutputMax = 0.19; // Max motor output in small PID mode
     public static final double kPracticeSmallTurnPIDOutputMax = 0.19; // Max motor output in small PID mode
 
     /* Hardware PID values for the Talon */
     static final double kDistP = 0.075; //Practice bot had .15 but was also overshooting.  
     static final double kDistI = 0.0;//0.005 on 2017 robot
     static final double kDistD = 0.0;
     static final double kDistF = 0.3789; // Use for MotionMagic.  You must set this to ZERO if using Position mode!!!!!
     static final double kAccelTime  = 0.625; // Time to reach full velocity
     static final double kTopSpeed = 2700; // Encoder units per 100us.
     static final int kAccelUnits = (int) (kTopSpeed/kAccelTime); //velocity to reach after 1 second (2700 is full speed)
     static final double kRightAccelTime = 0.625; // allow for a different accel on right side if needed
     static final int kRightAccelUnits= (int) (kTopSpeed/kRightAccelTime);
 
     // We also need LOCAL COPIES of the following values, which will start as the constants above but can be overridden by the SmartDashboard via updatePIDValuesFromSD()
     static double largeTurnP = kLargeTurnP;
     static double largeTurnI = kLargeTurnI;
     static double largeTurnD = kLargeTurnD;
     static double largeTurnF = kLargeTurnF;
     static double largeTurnPIDOutputMax = kLargeTurnPIDOutputMax;
     
 
     static double medTurnP = kMedTurnP;
     static double medTurnI = kMedTurnI;
     static double medTurnD = kMedTurnD;
     static double medTurnF = kMedTurnF;
     static double medTurnPIDOutputMax = kMedTurnPIDOutputMax;
 
     static double smallTurnP = kSmallTurnP;
     static double smallTurnI = kSmallTurnI;
     static double smallTurnD = kSmallTurnD;
     static double smallTurnF = kSmallTurnF;
     static double smallTurnPIDOutputMax = kSmallTurnPIDOutputMax;
   
}

