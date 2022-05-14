package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTBase {
	public int intValue;

	public NBTTagInt() {
	}

	public NBTTagInt(int i1) {
		this.intValue = i1;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeInt(this.intValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.intValue = dataInput1.readInt();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NBTTagInt){
			return intValue == ((NBTTagInt) obj).intValue;
		}
		return false;
	}

	public byte getType() {
		return (byte)3;
	}

	public String toString() {
		return "" + this.intValue;
	}
}
