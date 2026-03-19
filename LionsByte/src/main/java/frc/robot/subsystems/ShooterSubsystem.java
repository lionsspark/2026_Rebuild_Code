// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
/* 
package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Configs;
import frc.robot.Constants.ShooterSubsystemConstants.FeederSetpoints;
import frc.robot.Constants.ShooterSubsystemConstants.FlywheelSetpoints;
import frc.robot.Constants.ShooterSubsystemConstants;

public class ShooterSubsystem extends SubsystemBase {
  
  // Initialize flywheel SPARKs. We will use MAXMotion velocity control for the flywheel, so we also need to
  // initialize the closed loop controllers and encoders.
  private SparkFlex flywheelMotor =
      new SparkFlex(ShooterSubsystemConstants.kFlywheelMotorCanId, MotorType.kBrushless);
  private SparkClosedLoopController flywheelController = flywheelMotor.getClosedLoopController();
  private RelativeEncoder flywheelEncoder = flywheelMotor.getEncoder();

  private SparkFlex flywheelFollowerMotor =
      new SparkFlex(ShooterSubsystemConstants.kFlywheelFollowerMotorCanId, MotorType.kBrushless);

  // Initialize feeder SPARK. We will use open loop control for this so we don't need a closed loop
  // controller like above.
  private SparkFlex feederMotor =
      new SparkFlex(ShooterSubsystemConstants.kFeederMotorCanId, MotorType.kBrushless);

  // Member variables for subsystem state management
  private double flywheelTargetVelocity = 0.0;

  public ShooterSubsystem() {
    

    flywheelMotor.configure(
        Configs.ShooterSubsystem.flywheelConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    flywheelFollowerMotor.configure(
        Configs.ShooterSubsystem.flywheelFollowerConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    feederMotor.configure(
        Configs.ShooterSubsystem.feederConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

    // Zero flywheel encoder on initialization
    flywheelEncoder.setPosition(0);

    System.out.println("---> ShooterSubsystem initialized");
  }

  private boolean isFlywheelAt(double velocity) {
    return MathUtil.isNear(flywheelEncoder.getVelocity(), 
            velocity, FlywheelSetpoints.kVelocityTolerance);
  }

 
  public final Trigger isFlywheelSpinning = new Trigger(
      () -> isFlywheelAt(5000) || flywheelEncoder.getVelocity() > 5000
  );

  public final Trigger isFlywheelSpinningBackwards = new Trigger(
      () -> isFlywheelAt(-5000) || flywheelEncoder.getVelocity() < -5000
  );

  
  public final Trigger isFlywheelStopped = new Trigger(() -> isFlywheelAt(0));

  
   // Drive the flywheels to their set velocity. This will use MAXMotion
   // velocity control which will allow for a smooth acceleration and deceleration to the mechanism's
   // setpoint.
   
  private void setFlywheelVelocity(double velocity) {
    flywheelController.setSetpoint(velocity, ControlType.kMAXMotionVelocityControl);
    flywheelTargetVelocity = velocity;
  }

  // Set the feeder motor power in the range of [-1, 1]. 
  private void setFeederPower(double power) {
    feederMotor.set(power);
  }
  
  
  public Command runFlywheelCommand() {
    return this.startEnd(
        () -> {
          this.setFlywheelVelocity(FlywheelSetpoints.kShootRpm);
        },
        () -> {
          this.setFlywheelVelocity(0.0);
        }).withName("Spinning Up Flywheel");
  }

  public Command runFeederCommand() {
    return this.startEnd(
        () -> {
          this.setFlywheelVelocity(FlywheelSetpoints.kShootRpm);
          this.setFeederPower(FeederSetpoints.kFeed);
        }, () -> {
          this.setFlywheelVelocity(0.0);
          this.setFeederPower(0.0);
        }).withName("Feeding");
  }

 
  public Command runShooterCommand() {
    return this.startEnd(
      () -> this.setFlywheelVelocity(FlywheelSetpoints.kShootRpm),
      () -> flywheelMotor.stopMotor()
    ).until(isFlywheelSpinning).andThen(
      this.startEnd(
        () -> {
          this.setFlywheelVelocity(FlywheelSetpoints.kShootRpm);
          this.setFeederPower(FeederSetpoints.kFeed);
        }, () -> {
          flywheelMotor.stopMotor();
          feederMotor.stopMotor();
        })
    ).withName("Shooting");
  }

  @Override
  public void periodic() {
    // Display subsystem values
    SmartDashboard.putNumber("Shooter | Feeder | Applied Output", feederMotor.getAppliedOutput());
    SmartDashboard.putNumber("Shooter | Flywheel | Applied Output", flywheelMotor.getAppliedOutput());
    SmartDashboard.putNumber("Shooter | Flywheel | Current", flywheelMotor.getOutputCurrent());
    SmartDashboard.putNumber("Shooter | Flywheel Follower | Applied Output", flywheelFollowerMotor.getAppliedOutput());
    SmartDashboard.putNumber("Shooter | Flywheel Follower | Current", flywheelFollowerMotor.getOutputCurrent());

    SmartDashboard.putNumber("Shooter | Flywheel | Target Velocity", flywheelTargetVelocity);
    SmartDashboard.putNumber("Shooter | Flywheel | Actual Velocity", flywheelEncoder.getVelocity());

    SmartDashboard.putBoolean("Is Flywheel Spinning", isFlywheelSpinning.getAsBoolean());
    SmartDashboard.putBoolean("Is Flywheel Stopped", isFlywheelStopped.getAsBoolean());
  }

}
  */