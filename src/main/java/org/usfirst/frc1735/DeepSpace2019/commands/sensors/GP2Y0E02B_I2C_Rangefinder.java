/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1735.DeepSpace2019.commands.sensors;

import java.nio.ByteBuffer;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.ADXL345_I2C; // reference only
import edu.wpi.first.wpilibj.AnalogAccelerometer; // reference only
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Ultrasonic; // reference only
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * See spec sheet at:
 * http://www.socle-tech.com/doc/IC%20Channel%20Product/Sensors/Distance%20Measuring%20Sensor/Analog%20Out(Including%20I2C%20output)/GP2Y0E02B_SPEC.pdf
 * and a much more detailed application spec at
 * http://www.sharp-world.com/products/device/lineup/data/pdf/datasheet/gp2y0e02_03_appl_e.pdf
 */
public class GP2Y0E02B_I2C_Rangefinder extends SendableBase implements PIDSource {

    /**
     * Constructs the GP2Y0E02B Rangefinder with I2C address 0x80 (the spec'd default).
     *
     * @param port  The I2C port the accelerometer is attached to
     */
    public GP2Y0E02B_I2C_Rangefinder(I2C.Port port) {
        this(port, Unit.kInches); // Default to inches
    }

    public GP2Y0E02B_I2C_Rangefinder(I2C.Port port, Unit units) {
        this(port, units, kDeviceAddress); // Default to the spec'd device address
    }

    /**
     * Constructs the GP2Y0E02B Rangefinder over I2C.
     *
     * @param port          The I2C port the rangefinder is attached to
     * @param deviceAddress I2C address of the rangefinder (could be )
     */
    public GP2Y0E02B_I2C_Rangefinder(I2C.Port port, Unit units, int deviceAddress) {
      m_i2c = new I2C(port, deviceAddress);
      m_units = units;

      // FIXME:  do some initialization here (enable it?  configure it?)

      HAL.report(tResourceType.kResourceType_Ultrasonic, 1/*FIXME:  Assume just one instance for now*/);
      setName("GP2Y0E02B_I2C_Rangefinder", port.value);
    }
 
    /**
     * Set the current DistanceUnit that should be used for the PIDSource base object.
     *
     * @param units The DistanceUnit that should be used.
     */
    public void setDistanceUnits(Unit units) {
      m_units = units;
    }

    /**
     * Get the current DistanceUnit that is used for the PIDSource base object.
     *
     * @return The type of DistanceUnit that is being used.
     */
    public Unit getDistanceUnits() {
      return m_units;
    }


    public double getRangeInches() {
        return getRangeCM() / 2.54;
    }
    public double getRangeCM() {
        // Spec sheet actually says:
        // dist value = distance[11:0]/16/(2^n), in cm, where the coefficient n is the contents of the shift_bit reg

        // Distance comes in two different registers
        // DistLo is bits [3:0]
        ByteBuffer distLoBuf = ByteBuffer.allocate(1);
        m_i2c.read(kDistLoReg, 1, distLoBuf);
        byte distLo = distLoBuf.get(0);

        //DistHi is bigs [11:4]
        ByteBuffer distHiBuf = ByteBuffer.allocate(1);
        m_i2c.read(kDistHiReg, 1, distHiBuf);
        byte distHi = distHiBuf.get(0);

        // Build up the full raw/unscaled distance
        int rawDist = (distHi <<4 ) | distLo;

        // Extract the scaling coefficient
        ByteBuffer sBitBuf = ByteBuffer.allocate(1);
        m_i2c.read(kShiftBitReg, 1, sBitBuf); // Spec says default =0 (64cm max valid reading)
        byte sBit = sBitBuf.get(0);

        // Scott sez:  This next part makes no sense!
        // The dist is divided into the bottom 4 bits and the upper 8 bits.
        // The spec's algorithm says to divide the full distance by 16, which is the
        // binary equivalent of shifting by 4 positions (/2, /4, /8, /16)
        // Thus, dividing by 16 eliminates the bottom four bits, which is the content of the
        // dataLo register we just added in??!!!??  Seems like you could save a few operations
        // by simply implementing it as:
        //   value = distHi/(2^n).
        // But for now, implement as spec'd in case reads have side effects or something.
        // FIXME:  try seeing if the simpler implementation below matches!

        int dist = (int)(rawDist/16/(Math.pow(2, sBit)));
        // could just be
        //int dist2 = (int)(distHi/(Math.pow(2, sBit)));
        return dist;
    }


    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("I2C_Rangefinder");
        builder.addDoubleProperty("Value", this::getRangeInches, null);
    }
    
    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
      if (!pidSource.equals(PIDSourceType.kDisplacement)) {
        throw new IllegalArgumentException("Only displacement PID is allowed for ultrasonics.");
      }
      m_pidSource = pidSource;
    }
  
    @Override
    public PIDSourceType getPIDSourceType() {
      return m_pidSource;
    }

    @Override
    public void close() {
      super.close();
      m_i2c.close();
    }
     
   /**
   * Get the range in the current DistanceUnit for the PIDSource base object.
   *
   * @return The range in DistanceUnit
   */
  @Override
  public double pidGet() {
    switch (m_units) {
      case kInches:
        return getRangeInches();
      case kCentimeters:
        return getRangeCM();
      default:
        return 0.0;
    }
  }
 
    /*
     * Member Variables
     */
    protected PIDSourceType m_pidSource = PIDSourceType.kDisplacement;
    protected I2C m_i2c;
    // Register addresses for this device (from the spec).  Cast to byte to avoid signed int assumption
    private static final byte kDeviceAddress = (byte) 0x80; //FIXME:  Make sure this doesn't cause a 2s-complement sign inversion due to top bit being set!
    private static final byte kDistHiReg = (byte) 0x5e;
    private static final byte kDistLoReg = (byte) 0x5f;
    private static final byte kShiftBitReg = (byte) 0x35;
    private Unit m_units;
 
    /**
     * The units to return when PIDGet is called.
     */
    public enum Unit {
      /**
       * Use inches for PIDGet.
       */
      kInches,
      /**
       * Use centimeters for PIDGet.
       */
      kCentimeters
    }



    // FIXME:  Temporary reference objects to look at while coding this class
    //AnalogAccelerometer foo;
    //Ultrasonic bar;
    //ADXL345_I2C baz;
}
