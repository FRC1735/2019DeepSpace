// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1735.DeepSpace2019;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import org.usfirst.frc1735.DeepSpace2019.commands.AutonomousCommand;
import org.usfirst.frc1735.DeepSpace2019.commands.AutonomousExperiment;
import org.usfirst.frc1735.DeepSpace2019.smartdashboard.SmartDashboardKeys;
import org.usfirst.frc1735.DeepSpace2019.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in 
 * the project.
 */
public class Robot extends TimedRobot {

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static DriveTrain driveTrain;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static double DEFAULT_JOYSTICK_DEADZONE = 0.08;

    // Team-specific code for "global" members and variables
    // NAVX-MXP interface
    public static AHRS ahrs;

    // This variable can be used to turn off functionality like the drivetrain at demo events.
    // It is set/sampled from the SmartDashboard at robot enabling only.
    private static boolean demoMode = false;
    public static boolean isDemoMode() {
        return demoMode;
    }

    // Support for a "Debug" print mode
    private static boolean dbgOn = false;
    public static boolean isDbgOn() {
        return dbgOn;
    }
    // Wrapper to handle debug console messages cleanly
    public static void dbgPrintln(String message) {
    	if(isDbgOn()) {
    		System.out.println(message);
    	}
    }
    // Method to set the debug status from the SmartDashboard
    // This is called whenever we enable the robot, but can also be called by a Command button on the SD.
    public static void updateDbgModeFromSD() {
    	dbgOn = SmartDashboard.getBoolean("Master Debug Enable", false); // Second arg is default if entry not found
    }

    // Support for practice robot versus competition robot
    // This is only updated when we enable the robot.
    private static boolean practiceBot = false;
    public static boolean isPracticeBot() {
        return practiceBot;
    }



    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrain = new DriveTrain();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Add commands to Autonomous Sendable Chooser
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

        chooser.addObject("AutonomousExperiment", new AutonomousExperiment());
        chooser.setDefaultOption("Autonomous Command", new AutonomousCommand());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

        initializeSmartDashboard(); 
        try {
			/***********************************************************************
			 * navX-MXP:
			 * - Communication via RoboRIO MXP (SPI, I2C, TTL UART) and USB.            
			 * - See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface.
			 * 
			 * navX-Micro:
			 * - Communication via I2C (RoboRIO MXP or Onboard) and USB.
			 * - See http://navx-micro.kauailabs.com/guidance/selecting-an-interface.
			 * 
			 * Multiple navX-model devices on a single robot are supported.
			 ************************************************************************/
	        ahrs = new AHRS(SPI.Port.kMXP); 
	    } catch (RuntimeException ex ) {
	        DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
	    }
    	ahrs.zeroYaw(); // Init the gyro to zero degrees
    	
    	//Run some boot-time drivetrain initializtion (some final init will happen at teleop/autonomous init
    	Robot.driveTrain.drivetrainInit();

    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit(){

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        autonomousCommand = chooser.getSelected();
        // Perform any actions that only happen when enabling the robot
        this.performEnableActions();
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        this.performEnableActions();

        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    private void initializeSmartDashboard() {
        SmartDashboard.putData("Auto mode", chooser);
        SmartDashboard.putNumber(SmartDashboardKeys.JOYSTICK_DEADZONE, DEFAULT_JOYSTICK_DEADZONE);
        SmartDashboard.putString(SmartDashboardKeys.DRIVETRAIN_MODE, "ARCADE");
        
        // This button sets the master debug MODE
        // While this could be dynamically queried during robot operation, that would require many queries to the SmartDashboard.
        // instead, we want a local variable that samples the SmartDashboard only when the robot is getting enabled.
        // If the user desires to update the debug state dynamically, a Command button is provided at the SmartDashboard to force an update.
        SmartDashboard.putBoolean("Master Debug Enable", false);
        
        // Initialize "Demo Mode" variable for the SmartDashboard.
        // This is used to turn off joysticks, etc. at demo events (to avoid having to pull fuses)
        // Note:  it is only sampled when the robot is enabled!
        SmartDashboard.putBoolean("Demo Mode", false);

        // This variable controls any behaviors that differ between the Practice robot and the Competition robot.
        // This could be ignoring/disabling motors, changing PID values, etc.
        SmartDashboard.putBoolean("PracticeBot", false);

    }

    //User code function for performing actions only when the robot is getting enabled (either Autonomous or Teleop)
    public void performEnableActions () {
        // Sample the Robot's Demo mode and store its state
        demoMode = SmartDashboard.getBoolean("Demo Mode", false);

        // Sample the flag for printing debug messages (Get it from the Smart Dashboard)
        // This can be done via a command button on the SD, so it's implemented as a function
        Robot.updateDbgModeFromSD();

        // Sample the flag for whether the attached robot is a Practice robot, or the Competition robot
        practiceBot = SmartDashboard.getBoolean("PracticeBot", false);

        // Allow the SmartDashboard to override the compiled-in values for the PID values.
        // This could be disabled by commenting out the call, of course.
        DriveTrain.updatePIDValuesFromSD();

    }
}
