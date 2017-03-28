package org.usfirst.frc.team6620.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myDrive;
	Joystick left, right;
	Jaguar climber = new Jaguar(5);
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		myDrive = new RobotDrive(1,2,3,4);
		left = new Joystick(0);
		right = new Joystick(1);
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		myDrive.setInvertedMotor(MotorType.kFrontLeft, true);
		myDrive.setInvertedMotor(MotorType.kRearLeft, true);
		myDrive.setInvertedMotor(MotorType.kFrontRight, true);
		myDrive.setInvertedMotor(MotorType.kRearRight, true);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
			switch (autoSelected) {
			case customAuto:
				myDrive.drive(1.0, 0);
				Timer.delay(3); //TODO: Set appropriate forward movement duration
				myDrive.stopMotor();
				break;
			case defaultAuto:
				myDrive.drive(.5, 0);
				Timer.delay(2); //TODO: Set appropriate forward movement duration
				myDrive.stopMotor();
				break;
			default:
				break;
			}
	}
	public void disabledInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
		int controlScheme = 1;
		
		while (isOperatorControl() && isEnabled()) {
			
			
			//Check for control scheme switch...
			if (right.getRawButton(7)){
				if (right.getRawButton(9) && !right.getRawButton(11)) {
					controlScheme = 1;
				} else if (right.getRawButton(11) && !right.getRawButton(9)) {
					controlScheme = 2;
				}
			}
			
			
			//Run selected control scheme...
			switch (controlScheme) {
			case 1:
				if (!right.getRawButton(7)) {
					myDrive.tankDrive(left, right);
					Timer.delay(0.01);
					
					if (left.getRawButton(4)) {
						climber.set(-1.0);
					} else if (left.getRawButton(3)) {
						climber.set(-0.35);
					} else {
						climber.set(0.0);
					}	
				}
				break;
			
			case 2:
				if (!right.getRawButton(7)) {
					myDrive.arcadeDrive(right, true);
					Timer.delay(0.01);
					
				    if (left.getRawButton(4)) {
						climber.set(-1.0);
					} else if (left.getRawButton(3)) {
						climber.set(-0.35);
					} else {
						climber.set(0.0);
					}
				}
				break;
				
			default:
				if (!right.getRawButton(7)) {
					myDrive.tankDrive(left, right);
					Timer.delay(0.01);
					
			
					if (left.getRawButton(4)) {
						climber.set(-1.0);
					} else if (left.getRawButton(3)) {
						climber.set(-0.35);
					} else {
						climber.set(0.0);
					}	
				}
				break;
				
			}
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}
