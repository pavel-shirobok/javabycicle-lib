package ramshteks.java.bytes;

import ramshteks.java.utils.ByteUtils;

import java.util.LinkedList;

public class BytesArray implements IBytesArray{
	private LinkedList<Byte> list;

    public static BytesArray wrap(byte[] bytes){
        BytesArray result = new BytesArray();
        result.add(bytes);
        return result;
    }

	public BytesArray(){
		list=new LinkedList<Byte>();
	}

	public void add(byte value) {
		list.add(value);
	}

	public void add(byte[] value) {
		for (byte aValue : value) {
			add(aValue);
		}
	}

	public void addShort(short value) {
		add(ByteUtils.getShortBytes(value));
	}

	public void addInt(int value) {
		add(ByteUtils.getIntBytes(value));
	}

	public void addFloat(float value) {
		int v = Float.floatToRawIntBits(value);
		add(ByteUtils.getIntBytes(v));
	}

	public void addLong(long value) {
		add(ByteUtils.getLongBytes(value));
	}

	public int position() {
		return -1;
	}

	public void clear() {
		list.clear();
	}

	public int size() {
		return list.size();
	}

	public byte[] array() {
		byte[] result = new byte[size()];
		for(int i =0, len=list.size(); i<len; i++){
			result[i] = list.get(i);
		}
		return result;
	}
}
