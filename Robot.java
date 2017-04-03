package org.usfirst.frc.team6620.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
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
	Jaguar climberL = new Jaguar(5);
	Jaguar climberR = new Jaguar(6);
	
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	final String centerAuto = "Center Start";
	final String rightAuto = "Right Start";
	final String leftAuto = "Left Start";
	final String testAuto = "Testing";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	
	//QUICK UPDATE BANK
	float driveTrim = 0; //Finalize when balanced
	float curveTrim = 0; //Finalize when balanced
	final float autonomousSpeed = 0.5;
	final int autonomousDurationA = 100;
	final int autonomousDurationB = 150;
	System.out.println("WARNING: You are running an experimental build! Redeploy a stable build before competition!")

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
		chooser.addObject("Center Start Position", centerAuto);
		chooser.addObject("Right Start Position", rightAuto);
		chooser.addObject("Left Start Position", leftAuto);
		chooser.addObject("TEST", testAuto);
		SmartDashboard.putData("Auto choices", chooser);
		myDrive.setInvertedMotor(MotorType.kFrontLeft, true);
		myDrive.setInvertedMotor(MotorType.kRearLeft, true);
		myDrive.setInvertedMotor(MotorType.kFrontRight, true);
		myDrive.setInvertedMotor(MotorType.kRearRight, true);
		System.out.println("robotInit() executed!")
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
	}

	/**
	 * This function is called periodically during autonomous
	 */
	int autonomousTick = 0; //How many times autonomousPeriodic() has been iterated
	@Override
	public void autonomousPeriodic() {
			switch (autoSelected) {
			case customAuto:
				if (autonomousTick < autonomousDurationA) {
					myDrive.drive(-autonomousSpeed, -0.1);
				}
				if (autonomousTick > autonomousDurationA) {
					myDrive.stopMotor();
				}
				autonomousTick++;
				break;
			case centerAuto:
				System.out.println("INFO: Running center autonomous");
				if (autonomousTick < autonomousDurationA) {
					myDrive.drive(-autonomousSpeed, 0.0);
				}
				if (autonomousTick > autonomousDurationA) {
					myDrive.stopMotor();
				}
				autonomousTick++;
				break;
			case rightAuto:
				System.out.println("INFO: Running right autonomous");
				if (autonomousTick < autonomousDurationB) {
					myDrive.drive(-autonomousSpeed, curveTrim); //TODO: Correct curve
				}
				if (autonomousTick > autonomousDurationB) {
					myDrive.stopMotor();
				}
				autonomousTick++;
				break;
			case leftAuto:
				System.out.println("INFO: Running left autonomous");
				if (autonomousTick < autonomousDurationB) {
					myDrive.drive(-autonomousSpeed, -curveTrim); //TODO: Correct curve
				}
				if (autonomousTick > autonomousDurationB) {
					myDrive.stopMotor();
				}
				autonomousTick++;
				break;
			case defaultAuto:
				System.out.println("WARNING: Running default autonomous");
				if (autonomousTick < autonomousDurationB) {
					myDrive.drive(-autonomousSpeed, 0.0);
				}
				if (autonomousTick > autonomousDurationB) {
					myDrive.stopMotor();
				}
				autonomousTick++;
				break;
			case testAuto:
				System.out.println("WARNING: Running test autonomous");
				autonomousVerbose()
				if (autonomousTick < autonomousDurationB) {
					myDrive.drive(-autonomousSpeed, driveTrim + curveTrim);
				}
				if (autonomousTick > autonomousDurationB) {
					myDrive.stopMotor();
				}
				autonomousTick++;
				break;
			default:
				System.out.println("ERROR: Autonomous failed to correctly select a movement sequence.");
				break;
			}
	}
	
	public void autonomousVerbose() {
		System.out.println("Trim: " + driveTrim);
		System.out.println("Curve: " + curveTrim);
		System.out.println("Duration: " + autonomousDurationB + " ticks");
		System.out.println("Current Tick: " + autonomousTick);
	}
	
	public void disabledInit() {
		autonomousTick = 0;
	}
	
	public void climber(double power) {
		climberL.set(-power);
		climberR.set(power);
	}

	/**
	 * This function is called periodically during operator control
	 */
	
	int controlScheme = 1;
	/*
	 *  KEY:
	 * 1 : Tank Drive (default)
	 * 2 : Arcade Drive
	 * 
	 */
	boolean controlCurve = false; //Is squared inputs enabled
	
	@Override
	public void teleopPeriodic() {
		
		
		if (isOperatorControl() && isEnabled()) {
			
			
			//Check for control scheme switch...
			if (left.getRawButton(7)){
				if (left.getRawButton(9) && !left.getRawButton(11)) {
					controlScheme = 1;
					System.out.println("INFO: Tank Drive selected");
				} else if (left.getRawButton(11) && !left.getRawButton(9)) {
					controlScheme = 2;
					System.out.println("INFO: Arcade Drive selected");
				}
			}
			
			//Check for control curve switch...
			if (left.getRawButton(8)) {
				if (left.getRawButton(9) && !left.getRawButton(11)) {
					controlCurve = false;
					System.out.println("INFO: Squared Inputs disabled");
				} else if (left.getRawButton(11) && !left.getRawButton(9)) {
					controlCurve = true;
					System.out.println("INFO: Squared Inputs enabled");
				}
			}
			
			//Run selected control scheme...
			switch (controlScheme) {
			case 1: //Tank Drive
				if (!left.getRawButton(7)) {
					myDrive.tankDrive(left, right);
					
					//climber...
					if (left.getTrigger()) {
						climber(1);
					}
					else {
						climber(0);
					}
					
					//autonomous trimming...
					if (right.getRawButton(7)) { //top left base button to "straighten" left
						driveTrim += 0.001
					}
					if (right.getRawButton(8)) { //top right base button to "straighten" right
						driveTrim -= 0.001
					}
				}
				break;
			
			case 2: //Arcade Drive
				if (!left.getRawButton(7)) {
					myDrive.arcadeDrive(right, controlCurve);
					
					if (right.getTrigger()) {
						climber(1);
					}
					else {
						climber(0);
					}
				}
				break;
			
				
			default: //Tank Drive (default)
				if (!left.getRawButton(7)) {
					myDrive.tankDrive(left, right);
					
					if (left.getTrigger()) {
						climber(1);
					}
					else {
						climber(0);
					}
				}
				System.out.println("WARNING: Teleop is defaulting!");
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
