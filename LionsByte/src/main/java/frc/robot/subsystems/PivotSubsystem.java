package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkClosedLoopController.ArbFFUnits;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants.PivotSubsystemConstants;
import frc.robot.Configs;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class PivotSubsystem extends SubsystemBase{
    private SparkFlex pivotMotor = new SparkFlex(PivotSubsystemConstants.kPivotMotorCanId, MotorType.kBrushless);
    private SparkClosedLoopController pivotPID = pivotMotor.getClosedLoopController();
    private SparkAbsoluteEncoder absEncoder = pivotMotor.getAbsoluteEncoder();
    private double angle;
    //private double ffVolts;

public PivotSubsystem(){
    pivotMotor.configure(
    Configs.PivotSubsystem.pivotConfig,
    ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    angle = Math.toRadians(45);
}
   private ArmFeedforward ff = new ArmFeedforward(.1, .5, 0);

public void setAngle(double targetDegrees) {
        double ffVolts = ff.calculate(
        Math.toRadians(getAngle()),
        0
    );
    pivotPID.setSetpoint(
        targetDegrees,
        ControlType.kPosition,
        ClosedLoopSlot.kSlot0,
        ffVolts,
        ArbFFUnits.kVoltage
    );
}

public double getAngle() {
    return absEncoder.getPosition();
} 
private double targetAngle = 0;
    // constructor calls configurePivot() here

    public Command goToAngle(double degrees) {
        return Commands.run(() -> setAngle(degrees), this)
            .until(() -> Math.abs(getAngle() - degrees) < 2.0);
    }
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Pivot Angle", getAngle());
        SmartDashboard.putNumber("Pivot Target", targetAngle);
    }
}