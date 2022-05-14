package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTBase {
	public byte byteValue;

	public NBTTagByte() {
	}

	public NBTTagByte(byte b1) {
		this.byteValue = b1;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeByte(this.byteValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.byteValue = dataInput1.readByte();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NBTTagByte){
			return byteValue == ((NBTTagByte) obj).byteValue;
		}
		return false;
	}

	public byte getType() {
		return (byte)1;
	}

	public String toString() {
		return "" + this.byteValue;
	}
}
