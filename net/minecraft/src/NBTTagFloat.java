package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat extends NBTBase {
	public float floatValue;

	public NBTTagFloat() {
	}

	public NBTTagFloat(float f1) {
		this.floatValue = f1;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeFloat(this.floatValue);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.floatValue = dataInput1.readFloat();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NBTTagFloat){
			return floatValue == ((NBTTagFloat) obj).floatValue;
		}
		return false;
	}

	public byte getType() {
		return (byte)5;
	}

	public String toString() {
		return "" + this.floatValue;
	}
}
