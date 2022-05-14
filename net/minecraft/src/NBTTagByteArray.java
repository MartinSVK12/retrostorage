package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByteArray extends NBTBase {
	public byte[] byteArray;

	public NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] b1) {
		this.byteArray = b1;
	}

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		dataOutput1.writeInt(this.byteArray.length);
		dataOutput1.write(this.byteArray);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		int i2 = dataInput1.readInt();
		this.byteArray = new byte[i2];
		dataInput1.readFully(this.byteArray);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NBTTagByteArray) {
			int s = 0;
			for(int i = 0;i<byteArray.length;i++) {
				if (byteArray[i] == ((NBTTagByteArray) obj).byteArray[i]) {
					s++;
				}
			}
			return s == byteArray.length;
		}
		return false;
	}

	public byte getType() {
		return (byte)7;
	}

	public String toString() {
		return "[" + this.byteArray.length + " bytes]";
	}
}
