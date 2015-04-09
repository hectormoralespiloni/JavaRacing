///=============================================================================
///@file    FixedPoint.java
///@brief   Fixed Point class to represent float values in 16.16
///			16 bits represent the integral part and 16 bits the decimal
///
///@author  Héctor Morales Piloni
///@date    December 30, 2007
///=============================================================================

class FixedPoint 
{
	final private int FP_SHIFT = 16;
	final private int FP_SCALE = 0xFFFF;
	private int m_value;

	public FixedPoint() 
	{
		m_value = 0;
	}

	public FixedPoint(int value) 
	{
		m_value = value << FP_SHIFT;
	}

	public FixedPoint(double value) 
	{
		m_value = (int) (value * FP_SCALE);
	}

	///=========================================================================
	///GetValue
	///@returns int the value (int) of this fixed point number
	///=========================================================================
	public int GetValue() 
	{
		return m_value;
	}

	///=========================================================================
	///GetIntegral
	///@returns int the integral part of the fixed point number
	///=========================================================================
	public int GetIntegral() 
	{
		//get the upper 16 bits
		return (m_value >> FP_SHIFT);
	}

	///=========================================================================
	///GetDecimal
	///@returns int the decimal part of the fixed point number
	///=========================================================================  
	public int GetDecimal() 
	{
		//mask the integral part
		return (m_value & 0x0000FFFF);
	}

	///=========================================================================
	///Performs Fixed point multiplication
	///@param fp FixedPoint is the multiplicand operand
	///=========================================================================
	public int Mul(FixedPoint fp) 
	{
		//shift to avoid double scaling
		return ((m_value * fp.GetValue()) >> FP_SHIFT);
	}

	public int Mul(int i) 
	{
		FixedPoint fp = new FixedPoint(i);
		return Mul(fp);
	}

	public int Mul(double d) 
	{
		FixedPoint fp = new FixedPoint(d);
		return Mul(fp);
	}

	///=========================================================================
	///Performs Fixed point division
	///@param fp FixedPoint is the divisor operand
	///=========================================================================
	public int Div(FixedPoint fp) 
	{
		//pre-scale to maintain the fixed point property
		return (m_value << FP_SHIFT) / fp.GetValue();
	}

	public int Div(int i) 
	{
		FixedPoint fp = new FixedPoint(i);
		return Div(fp);
	}

	public int Div(double d) 
	{
		FixedPoint fp = new FixedPoint(d);
		return Div(fp);
	}
}
