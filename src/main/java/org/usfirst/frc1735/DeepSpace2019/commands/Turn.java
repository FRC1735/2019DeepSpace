// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1735.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc1735.DeepSpace2019.Robot;
import org.usfirst.frc1735.DeepSpace2019.subsystems.DriveTrain;

/**
 *
 */
public class Turn extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private double m_angle;
    private int m_mode;
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public Turn(double angle, int mode) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_angle = angle;
        m_mode = mode;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Calling Turn without any argument pulls turn angle from the SmartDashboard
    public Turn() {
    	m_getDataFromSmartDashboard = true;
        requires(Robot.driveTrain);
		SmartDashboard.putString("PID Mode", "TBD");

    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	if (m_getDataFromSmartDashboard) {
    		// If we called this command without arguments, we need to get them from the SmartDashboard.
        	if (SmartDashboard.getBoolean("TurnAbsolute", true)) {
        		m_mode = DriveTrain.kAbsolute; 
        	}
        	else {
        		m_mode = DriveTrain.kRelative;
        	}
        	
           	m_angle      = SmartDashboard.getNumber("TurnAngle", 0); // Assuming relative, zero means no change.
    	}
    	else if (m_mode == DriveTrain.kCamera) {    		
    		m_angle = 0; // was calcCubeAngle(); // Use our heuristics to capture a cube on the camera and calculate its relative angle from our current position
    	}

    	
    	// Convert any relative angles to field-absolute, if necessary
    	if ((m_mode == DriveTrain.kRelative) ||
    		(m_mode == DriveTrain.kCamera)) {
    		// We are in RELATIVE mode here (either explicitly, or implicitly in camera mode)
        	double startAbsAngle = Robot.ahrs.getYaw(); // Get our current abs angle
        	m_targetAbsAngle = startAbsAngle + m_angle; // calc our new abs angle based on the relative turn we received as a param
        	if (m_targetAbsAngle > 180.0) {
        		m_targetAbsAngle = m_targetAbsAngle - 360.0;// Correct the angle to lie within -180:180 
        	}
        	if (m_targetAbsAngle < -180.0) {
        		m_targetAbsAngle = m_targetAbsAngle + 360.0;// Correct the angle to lie within -180:180 
        	}
    	}
    	else {
    		//We are in abs mode here
    		m_targetAbsAngle = m_angle; //abs angle was passed to us
    	}
    	
    	// Finally, enable the turn controller
    	Robot.driveTrain.drivelineController.setSetpoint(m_targetAbsAngle);	
    	Robot.driveTrain.drivelineController.enable();
    	System.out.println("Mode = " + m_mode + " ReqAngle= " + m_angle + " absTarget= " + m_targetAbsAngle);
    	// Reset the iteration counter (for reduced printing)
    	m_exeCount = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	if (m_mode == DriveTrain.kCamera) {
    		// in camera mode, we always recalculate the target based on the current camera image
        	double startAbsAngle = Robot.ahrs.getYaw(); // Get our current abs angle
        	double camAngle = 0; // was calcCubeAngle(); // Use our heuristics to capture a cube on the camera and calculate its relative angle from our current position

        	m_targetAbsAngle = startAbsAngle + camAngle; // calc our new abs angle based on camera input
        	if (m_targetAbsAngle > 180.0) {
        		m_targetAbsAngle = m_targetAbsAngle - 360.0;// Correct the angle to lie within -180:180 
        	}
        	if (m_targetAbsAngle < -180.0) {
        		m_targetAbsAngle = m_targetAbsAngle + 360.0;// Correct the angle to lie within -180:180 
        	}
        	
        	// update the setpoint 
        	Robot.driveTrain.drivelineController.setSetpoint(m_targetAbsAngle);
        	
        	// increment a counter
        	m_exeCount++;
    	}

    	
    	double err = Robot.driveTrain.drivelineController.getError();
    	// For small errors (say, less than 5 degrees), run the PID with a low max and a high P-- this gets us a pretty constant but slow speed for small adjustments
    	// This avoids the "small P -> small motor values -> rely on very slow I values to get to the target
    	if (Math.abs(err) < Robot.driveTrain.kSmallTurnPIDLimit /*degrees*/) {
    		Robot.driveTrain.setSmallTurnPID();
    		SmartDashboard.putString("PID Mode", "Small");
    	}
    	else if (Math.abs(err) < Robot.driveTrain.kMedTurnPIDLimit /*degrees*/) {
    		Robot.driveTrain.setMedTurnPID();
    		SmartDashboard.putString("PID Mode", "Med");
    	}
    	else {
    		Robot.driveTrain.setLargeTurnPID(); // Otherwise use the normal PID values  
    		SmartDashboard.putString("PID Mode", "Large");
    	}
    	Robot.driveTrain.tankDrive(Robot.driveTrain.m_rotateToAngleRate, -Robot.driveTrain.m_rotateToAngleRate);
    	SmartDashboard.putNumber("MotorOut", Robot.driveTrain.m_rotateToAngleRate);
    	SmartDashboard.putNumber("PIDErr", err);
    }


    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
//        return (Robot.driveTrain.drivelineController.onTarget()) && // Ask controller if our current position is "close enough"
//        		(Math.abs(Robot.driveTrain.m_rotateToAngleRate) < Robot.driveTrain.kSmallTurnPIDOutputMax); // And the motors are low to no output, so we aren't actively oscillating around the target value
    	
    	// terminate when we have been "onTarget" for a minimum number of iterations
    	if (Robot.driveTrain.drivelineController.onTarget())
    		m_onTargetCount++; // if we are on target, increment the counter
    	else
    		m_onTargetCount = 0; // If we are not on target, reset the counter
    	
    	return (m_onTargetCount > 10);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.driveTrain.drivelineController.disable(); // Stop the turn controller
    	Robot.driveTrain.stop(); // Stop the motors;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	end();
    }
    

	// Multiple cubes may be in view in our autonomous, from the row against the switch fence.
	// Our heuristic is to choose the "outermost" detected cube,
	// but not "outwards" from our current position (to avoid possibly capturing a sidewall reflection)
	/**
	 * @param tx
	 * @param angleLimit
	 * @return
	 */

    
    //Member variables
    double m_targetAbsAngle; //the field relative angle that we want to end up at
    boolean m_getDataFromSmartDashboard = false;
    double m_onTargetCount = 0; // counter for determining whether we have settled on our target.
    int m_exeCount = 0; // Counter for number of times we have been called (i.e. every 20 ms it increments)
}
