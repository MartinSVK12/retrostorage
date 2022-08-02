package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class NBTTagCompound extends NBTBase {
	private HashMap tagMap = new HashMap();

	void writeTagContents(DataOutput dataOutput1) throws IOException {
		Iterator iterator2 = this.tagMap.values().iterator();

		while(iterator2.hasNext()) {
			NBTBase nBTBase3 = (NBTBase)iterator2.next();
			NBTBase.writeTag(nBTBase3, dataOutput1);
		}

		dataOutput1.writeByte(0);
	}

	void readTagContents(DataInput dataInput1) throws IOException {
		this.tagMap.clear();

		NBTBase nBTBase2;
		while((nBTBase2 = NBTBase.readTag(dataInput1)).getType() != 0) {
			this.tagMap.put(nBTBase2.getKey(), nBTBase2);
		}

	}

	public Collection func_28110_c() {return this.tagMap.values();}

	public Collection getValues() {
		return this.tagMap.values();
	}

	public Set getKeys(){
		return this.tagMap.keySet();
	}

	public int size() {
		return this.tagMap.size();
	}

	public byte getType() {
		return (byte)10;
	}

	public void setTag(String string1, NBTBase nBTBase2) {
		this.tagMap.put(string1, nBTBase2.setKey(string1));
	}

	public void setByte(String string1, byte b2) {
		this.tagMap.put(string1, (new NBTTagByte(b2)).setKey(string1));
	}

	public void setShort(String string1, short s2) {
		this.tagMap.put(string1, (new NBTTagShort(s2)).setKey(string1));
	}

	public void setInteger(String string1, int i2) {
		this.tagMap.put(string1, (new NBTTagInt(i2)).setKey(string1));
	}

	public void setLong(String string1, long j2) {
		this.tagMap.put(string1, (new NBTTagLong(j2)).setKey(string1));
	}

	public void setFloat(String string1, float f2) {
		this.tagMap.put(string1, (new NBTTagFloat(f2)).setKey(string1));
	}

	public void setDouble(String string1, double d2) {
		this.tagMap.put(string1, (new NBTTagDouble(d2)).setKey(string1));
	}

	public void setString(String string1, String string2) {
		this.tagMap.put(string1, (new NBTTagString(string2)).setKey(string1));
	}

	public void setByteArray(String string1, byte[] b2) {
		this.tagMap.put(string1, (new NBTTagByteArray(b2)).setKey(string1));
	}

	public void setCompoundTag(String string1, NBTTagCompound nBTTagCompound2) {
		this.tagMap.put(string1, nBTTagCompound2.setKey(string1));
	}

	public void setBoolean(String string1, boolean z2) {
		this.setByte(string1, (byte)(z2 ? 1 : 0));
	}

	public boolean hasKey(String string1) {
		return this.tagMap.containsKey(string1);
	}

	public byte getByte(String string1) {
		return !this.tagMap.containsKey(string1) ? 0 : ((NBTTagByte)this.tagMap.get(string1)).byteValue;
	}

	public short getShort(String string1) {
		return !this.tagMap.containsKey(string1) ? 0 : ((NBTTagShort)this.tagMap.get(string1)).shortValue;
	}

	public int getInteger(String string1) {
		return !this.tagMap.containsKey(string1) ? 0 : ((NBTTagInt)this.tagMap.get(string1)).intValue;
	}

	public long getLong(String string1) {
		return !this.tagMap.containsKey(string1) ? 0L : ((NBTTagLong)this.tagMap.get(string1)).longValue;
	}

	public float getFloat(String string1) {
		return !this.tagMap.containsKey(string1) ? 0.0F : ((NBTTagFloat)this.tagMap.get(string1)).floatValue;
	}

	public double getDouble(String string1) {
		return !this.tagMap.containsKey(string1) ? 0.0D : ((NBTTagDouble)this.tagMap.get(string1)).doubleValue;
	}

	public String getString(String string1) {
		return !this.tagMap.containsKey(string1) ? "" : ((NBTTagString)this.tagMap.get(string1)).stringValue;
	}

	public byte[] getByteArray(String string1) {
		return !this.tagMap.containsKey(string1) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(string1)).byteArray;
	}

	public NBTTagCompound getCompoundTag(String string1) {
		return !this.tagMap.containsKey(string1) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(string1);
	}

	public NBTTagList getTagList(String string1) {
		return !this.tagMap.containsKey(string1) ? new NBTTagList() : (NBTTagList)this.tagMap.get(string1);
	}

	public boolean getBoolean(String string1) {
		return this.getByte(string1) != 0;
	}

	public void removeTag(String s){
		this.tagMap.remove(s);
	}

	public NBTTagCompound copy(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.tagMap = (HashMap) tagMap.clone();
		return nbt;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NBTTagCompound){
			if(tagMap.size() == 0 && ((NBTTagCompound) obj).tagMap.size() == 0){
				return true;
			}
			if(tagMap.size() == 0 && ((NBTTagCompound) obj).tagMap.size() != 0){
				return false;
			} else if (tagMap.size() != 0 && ((NBTTagCompound) obj).tagMap.size() == 0){
				return false;
			}
			Iterator itrt = tagMap.entrySet().iterator();
			int s = 0;
			int i = 1;
			while (itrt.hasNext()) {
				Map.Entry<?,?> element = (Map.Entry<?, ?>) itrt.next();
				if(((NBTTagCompound) obj).hasKey((String) element.getKey())){
					if(element.getValue().equals(((NBTTagCompound) obj).tagMap.get(element.getKey()))){
						s++;
					}
				}
				i++;
			}
			return s == tagMap.size();
		}
		return false;
	}

	public String toString() { //return (new StringBuilder()).append("").append(tagMap).toString();
		return "" + this.tagMap.size() + " entries";
	}

	public String toStringExtended() {
		return (new StringBuilder()).append("").append(tagMap).toString();
	}
}
