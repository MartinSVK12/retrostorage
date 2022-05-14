package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
	public String stringValue;

	public NBTTagString() {
	}

	public NBTTagString(String string1) {
		this.stringValue = string1;
		if(string1 == null) {
			throw new IllegalArgumentException("Empty string not allowed");
		}
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeUTF(this.stringValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.stringValue = dataInput1.readUTF();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NBTTagString){
			return stringValue.equals(((NBTTagString) obj).stringValue);
		}
		return false;
	}

	public byte getType() {
		return (byte)8;
	}

	public String toString() {
		return "" + this.stringValue;
	}
}
