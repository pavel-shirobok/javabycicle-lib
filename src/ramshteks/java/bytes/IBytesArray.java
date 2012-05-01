package ramshteks.java.bytes;

public interface IBytesArray {

	void add(byte value);
	void add(byte[] value);
	void addShort(short value);
	void addInt(int value);
	void addFloat(float value);
	void addLong(long value);



	int position();
	void clear();
	int size();
	byte[] array();
}
