package net.minecraft.src;

import net.minecraft.client.Minecraft;

import net.sunsetsatellite.retrostorage.TileEntityInNetwork;
import org.lwjgl.opengl.GL11;

public class RenderBlocks {
	public IBlockAccess blockAccess;
	public int overrideBlockTexture;
	public boolean flipTexture;
	public boolean renderAllFaces;
	public static boolean fancyGrass = true;
	public static boolean cfgGrassFix = true;
	public boolean field_31088_b;
	public int field_31087_g;
	public int field_31086_h;
	public int field_31085_i;
	public int field_31084_j;
	public int field_31083_k;
	public int field_31082_l;
	public boolean enableAO;
	public float lightValueOwn;
	public float aoLightValueXNeg;
	public float aoLightValueYNeg;
	public float aoLightValueZNeg;
	public float aoLightValueXPos;
	public float aoLightValueYPos;
	public float aoLightValueZPos;
	public float field_22377_m;
	public float field_22376_n;
	public float field_22375_o;
	public float field_22374_p;
	public float field_22373_q;
	public float field_22372_r;
	public float field_22371_s;
	public float field_22370_t;
	public float field_22369_u;
	public float field_22368_v;
	public float field_22367_w;
	public float field_22366_x;
	public float field_22365_y;
	public float field_22364_z;
	public float field_22362_A;
	public float field_22360_B;
	public float field_22358_C;
	public float field_22356_D;
	public float field_22354_E;
	public float field_22353_F;
	public int field_22352_G;
	public float colorRedTopLeft;
	public float colorRedBottomLeft;
	public float colorRedBottomRight;
	public float colorRedTopRight;
	public float colorGreenTopLeft;
	public float colorGreenBottomLeft;
	public float colorGreenBottomRight;
	public float colorGreenTopRight;
	public float colorBlueTopLeft;
	public float colorBlueBottomLeft;
	public float colorBlueBottomRight;
	public float colorBlueTopRight;
	public boolean field_22339_T;
	public boolean field_22338_U;
	public boolean field_22337_V;
	public boolean field_22336_W;
	public boolean field_22335_X;
	public boolean field_22334_Y;
	public boolean field_22333_Z;
	public boolean field_22363_aa;
	public boolean field_22361_ab;
	public boolean field_22359_ac;
	public boolean field_22357_ad;
	public boolean field_22355_ae;
	public static float[][] redstoneColors = new float[16][];

	public RenderBlocks(IBlockAccess iblockaccess) {
		this();
		this.blockAccess = iblockaccess;
	}

	public RenderBlocks() {
		this.blockAccess = null;
		this.overrideBlockTexture = 0;
		this.flipTexture = false;
		this.renderAllFaces = false;
		this.field_31088_b = false;
		this.field_31087_g = 0;
		this.field_31086_h = 0;
		this.field_31085_i = 0;
		this.field_31084_j = 0;
		this.field_31083_k = 0;
		this.field_31082_l = 0;
		this.enableAO = false;
		this.lightValueOwn = 0.0F;
		this.aoLightValueXNeg = 0.0F;
		this.aoLightValueYNeg = 0.0F;
		this.aoLightValueZNeg = 0.0F;
		this.aoLightValueXPos = 0.0F;
		this.aoLightValueYPos = 0.0F;
		this.aoLightValueZPos = 0.0F;
		this.field_22377_m = 0.0F;
		this.field_22376_n = 0.0F;
		this.field_22375_o = 0.0F;
		this.field_22374_p = 0.0F;
		this.field_22373_q = 0.0F;
		this.field_22372_r = 0.0F;
		this.field_22371_s = 0.0F;
		this.field_22370_t = 0.0F;
		this.field_22369_u = 0.0F;
		this.field_22368_v = 0.0F;
		this.field_22367_w = 0.0F;
		this.field_22366_x = 0.0F;
		this.field_22365_y = 0.0F;
		this.field_22364_z = 0.0F;
		this.field_22362_A = 0.0F;
		this.field_22360_B = 0.0F;
		this.field_22358_C = 0.0F;
		this.field_22356_D = 0.0F;
		this.field_22354_E = 0.0F;
		this.field_22353_F = 0.0F;
		this.field_22352_G = 0;
		this.colorRedTopLeft = 0.0F;
		this.colorRedBottomLeft = 0.0F;
		this.colorRedBottomRight = 0.0F;
		this.colorRedTopRight = 0.0F;
		this.colorGreenTopLeft = 0.0F;
		this.colorGreenBottomLeft = 0.0F;
		this.colorGreenBottomRight = 0.0F;
		this.colorGreenTopRight = 0.0F;
		this.colorBlueTopLeft = 0.0F;
		this.colorBlueBottomLeft = 0.0F;
		this.colorBlueBottomRight = 0.0F;
		this.colorBlueTopRight = 0.0F;
		this.field_22339_T = false;
		this.field_22338_U = false;
		this.field_22337_V = false;
		this.field_22336_W = false;
		this.field_22335_X = false;
		this.field_22334_Y = false;
		this.field_22333_Z = false;
		this.field_22363_aa = false;
		this.field_22361_ab = false;
		this.field_22359_ac = false;
		this.field_22357_ad = false;
		this.field_22355_ae = false;
		this.overrideBlockTexture = -1;
		this.flipTexture = false;
		this.renderAllFaces = false;
		this.field_31088_b = true;
		this.field_31087_g = 0;
		this.field_31086_h = 0;
		this.field_31085_i = 0;
		this.field_31084_j = 0;
		this.field_31083_k = 0;
		this.field_31082_l = 0;
		this.field_22352_G = 1;
	}

	public void renderBlockUsingTexture(Block block, int i, int j, int k, int l) {
		this.overrideBlockTexture = l;
		this.renderBlockByRenderType(block, i, j, k);
		this.overrideBlockTexture = -1;
	}

	public void func_31075_a(Block block, int i, int j, int k) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(block, i, j, k);
		this.renderAllFaces = false;
	}

	public boolean renderBlockByRenderType(Block block, int i, int j, int k) {
		int l = block.getRenderType();
		block.setBlockBoundsBasedOnState(this.blockAccess, i, j, k);
		return l == 0 ? this.renderStandardBlock(block, i, j, k) : (l == 4 ? this.renderBlockFluids(block, i, j, k) : (l == 13 ? this.renderBlockCactus(block, i, j, k) : (l == 1 ? this.renderBlockReed(block, i, j, k) : (l == 6 ? this.renderBlockCrops(block, i, j, k) : (l == 2 ? this.renderBlockTorch(block, i, j, k) : (l == 3 ? this.renderBlockFire(block, i, j, k) : (l == 5 ? this.renderBlockRedstoneWire(block, i, j, k) : (l == 8 ? this.renderBlockLadder(block, i, j, k) : (l == 7 ? this.renderBlockDoor(block, i, j, k) : (l == 9 ? this.renderBlockMinecartTrack((BlockRail)block, i, j, k) : (l == 10 ? this.renderBlockStairs(block, i, j, k) : (l == 11 ? this.renderBlockFence(block, i, j, k) : (l == 12 ? this.renderBlockLever(block, i, j, k) : (l == 14 ? this.renderBlockBed(block, i, j, k) : (l == 15 ? this.renderBlockRepeater(block, i, j, k) : (l == 16 ? this.func_31074_b(block, i, j, k, false) : (l == 17 ? this.func_31080_c(block, i, j, k, true) : (l == 18 ? this.renderBlockCable(block, i, j, k) : ModLoader.RenderWorldBlock(this, this.blockAccess, i, j, k, block, l)))))))))))))))))));
	}

	private boolean renderBlockCable(Block block, int i, int j, int k) {

		float f = 0.4f;
		float f1 = (1.0F - f) / 2.0F;
		block.setBlockBounds(f1, f1, f1, f1 + f, f1 + f, f1 + f);
		renderStandardBlock(block, i, j, k);
		boolean flag = blockAccess.getBlockId(i + 1, j, k) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i + 1, j, k) instanceof TileEntityInNetwork ? true : false;
		boolean flag1 = blockAccess.getBlockId(i - 1, j, k) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i - 1, j, k) instanceof TileEntityInNetwork ? true : false;
		boolean flag2 = blockAccess.getBlockId(i, j + 1, k) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i, j + 1, k) instanceof TileEntityInNetwork ? true : false;
		boolean flag3 = blockAccess.getBlockId(i, j - 1, k) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i, j - 1, k) instanceof TileEntityInNetwork ? true : false;
		boolean flag4 = blockAccess.getBlockId(i, j, k + 1) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i, j, k + 1) instanceof TileEntityInNetwork ? true : false;
		boolean flag5 = blockAccess.getBlockId(i , j, k - 1) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i, j, k - 1) instanceof TileEntityInNetwork ? true : false;
		if(flag)
		{
			block.setBlockBounds(f1 + f, f1, f1, 1.0F, f1 + f, f1 + f);
			renderStandardBlock(block, i, j, k);
		}
		if(flag2)
		{
			block.setBlockBounds(f1, f1 + f, f1, f1 + f, 1.0F, f1 + f);
			renderStandardBlock(block, i, j, k);
		}
		if(flag4)
		{
			block.setBlockBounds(f1, f1, f1 + f, f1 + f, f1 + f, 1.0F);
			renderStandardBlock(block, i, j, k);
		}
		if(flag1)
		{
			block.setBlockBounds(0.0F, f1, f1, f1, f1 + f, f1 + f);
			renderStandardBlock(block, i, j, k);
		}
		if(flag3)
		{
			block.setBlockBounds(f1, 0.0F, f1, f1 + f, f1, f1 + f);
			renderStandardBlock(block, i, j, k);
		}
		if(flag5)
		{
			block.setBlockBounds(f1, f1, 0.0F, f1 + f, f1 + f, f1);
			renderStandardBlock(block, i, j, k);
		}
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockBed(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		int i1 = BlockBed.getDirectionFromMetadata(l);
		boolean flag = BlockBed.isBlockFootOfBed(l);
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		float f16 = block.getBlockBrightness(this.blockAccess, i, j, k);
		tessellator.setColorOpaque_F(f * f16, f * f16, f * f16);
		int j1 = block.getBlockTexture(this.blockAccess, i, j, k, 0);
		int k1 = (j1 & 15) << 4;
		int l1 = j1 & 240;
		double d = (double)((float)k1 / 256.0F);
		double d1 = ((double)(k1 + 16) - 0.01D) / 256.0D;
		double d2 = (double)((float)l1 / 256.0F);
		double d3 = ((double)(l1 + 16) - 0.01D) / 256.0D;
		double d4 = (double)i + block.minX;
		double d5 = (double)i + block.maxX;
		double d6 = (double)j + block.minY + 0.1875D;
		double d7 = (double)k + block.minZ;
		double d8 = (double)k + block.maxZ;
		tessellator.addVertexWithUV(d4, d6, d8, d, d3);
		tessellator.addVertexWithUV(d4, d6, d7, d, d2);
		tessellator.addVertexWithUV(d5, d6, d7, d1, d2);
		tessellator.addVertexWithUV(d5, d6, d8, d1, d3);
		float f17 = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
		tessellator.setColorOpaque_F(f1 * f17, f1 * f17, f1 * f17);
		k1 = block.getBlockTexture(this.blockAccess, i, j, k, 1);
		l1 = (k1 & 15) << 4;
		d = (double)(k1 & 240);
		double d9 = (double)((float)l1 / 256.0F);
		double d10 = ((double)(l1 + 16) - 0.01D) / 256.0D;
		double d11 = (double)((float)d / 256.0F);
		double d12 = (d + 16.0D - 0.01D) / 256.0D;
		double d13 = d9;
		double d14 = d10;
		double d15 = d11;
		double d16 = d11;
		double d17 = d9;
		double d18 = d10;
		double d19 = d12;
		double d20 = d12;
		if(i1 == 0) {
			d14 = d9;
			d15 = d12;
			d17 = d10;
			d20 = d11;
		} else if(i1 == 2) {
			d13 = d10;
			d16 = d12;
			d18 = d9;
			d19 = d11;
		} else if(i1 == 3) {
			d13 = d10;
			d16 = d12;
			d18 = d9;
			d19 = d11;
			d14 = d9;
			d15 = d12;
			d17 = d10;
			d20 = d11;
		}

		double d21 = (double)i + block.minX;
		double d22 = (double)i + block.maxX;
		double d23 = (double)j + block.maxY;
		double d24 = (double)k + block.minZ;
		double d25 = (double)k + block.maxZ;
		tessellator.addVertexWithUV(d22, d23, d25, d17, d19);
		tessellator.addVertexWithUV(d22, d23, d24, d13, d15);
		tessellator.addVertexWithUV(d21, d23, d24, d14, d16);
		tessellator.addVertexWithUV(d21, d23, d25, d18, d20);
		f17 = (float)ModelBed.field_22280_a[i1];
		if(flag) {
			f17 = (float)ModelBed.field_22280_a[ModelBed.field_22279_b[i1]];
		}

		byte k11 = 4;
		switch(i1) {
		case 0:
			k11 = 5;
			break;
		case 1:
			k11 = 3;
		case 2:
		default:
			break;
		case 3:
			k11 = 2;
		}

		float f21;
		if(f17 != 2.0F && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j, k - 1, 2))) {
			f21 = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
			if(block.minZ > 0.0D) {
				f21 = f16;
			}

			tessellator.setColorOpaque_F(f2 * f21, f2 * f21, f2 * f21);
			this.flipTexture = k11 == 2;
			this.renderEastFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 2));
		}

		if(f17 != 3.0F && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j, k + 1, 3))) {
			f21 = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
			if(block.maxZ < 1.0D) {
				f21 = f16;
			}

			tessellator.setColorOpaque_F(f2 * f21, f2 * f21, f2 * f21);
			this.flipTexture = k11 == 3;
			this.renderWestFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 3));
		}

		if(f17 != 4.0F && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i - 1, j, k, 4))) {
			f21 = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
			if(block.minX > 0.0D) {
				f21 = f16;
			}

			tessellator.setColorOpaque_F(f3 * f21, f3 * f21, f3 * f21);
			this.flipTexture = k11 == 4;
			this.renderNorthFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 4));
		}

		if(f17 != 5.0F && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i + 1, j, k, 5))) {
			f21 = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
			if(block.maxX < 1.0D) {
				f21 = f16;
			}

			tessellator.setColorOpaque_F(f3 * f21, f3 * f21, f3 * f21);
			this.flipTexture = k11 == 5;
			this.renderSouthFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 5));
		}

		this.flipTexture = false;
		return true;
	}

	public boolean renderBlockTorch(Block block, int i, int j, int k) {
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(this.blockAccess, i, j, k);
		if(Block.lightValue[block.blockID] > 0) {
			f = 1.0F;
		}

		tessellator.setColorOpaque_F(f, f, f);
		double d = (double)0.4F;
		double d1 = 0.5D - d;
		double d2 = (double)0.2F;
		if(l == 1) {
			this.renderTorchAtAngle(block, (double)i - d1, (double)j + d2, (double)k, -d, 0.0D);
		} else if(l == 2) {
			this.renderTorchAtAngle(block, (double)i + d1, (double)j + d2, (double)k, d, 0.0D);
		} else if(l == 3) {
			this.renderTorchAtAngle(block, (double)i, (double)j + d2, (double)k - d1, 0.0D, -d);
		} else if(l == 4) {
			this.renderTorchAtAngle(block, (double)i, (double)j + d2, (double)k + d1, 0.0D, d);
		} else {
			this.renderTorchAtAngle(block, (double)i, (double)j, (double)k, 0.0D, 0.0D);
		}

		return true;
	}

	public boolean renderBlockRepeater(Block block, int i, int j, int k) {
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		int i1 = l & 3;
		int j1 = (l & 12) >> 2;
		this.renderStandardBlock(block, i, j, k);
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(this.blockAccess, i, j, k);
		if(Block.lightValue[block.blockID] > 0) {
			f = (f + 1.0F) * 0.5F;
		}

		tessellator.setColorOpaque_F(f, f, f);
		double d = -0.1875D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		double d3 = 0.0D;
		double d4 = 0.0D;
		switch(i1) {
		case 0:
			d4 = -0.3125D;
			d2 = BlockRedstoneRepeater.field_22024_a[j1];
			break;
		case 1:
			d3 = 0.3125D;
			d1 = -BlockRedstoneRepeater.field_22024_a[j1];
			break;
		case 2:
			d4 = 0.3125D;
			d2 = -BlockRedstoneRepeater.field_22024_a[j1];
			break;
		case 3:
			d3 = -0.3125D;
			d1 = BlockRedstoneRepeater.field_22024_a[j1];
		}

		this.renderTorchAtAngle(block, (double)i + d1, (double)j + d, (double)k + d2, 0.0D, 0.0D);
		this.renderTorchAtAngle(block, (double)i + d3, (double)j + d, (double)k + d4, 0.0D, 0.0D);
		int k1 = block.getBlockTextureFromSide(1);
		int l1 = (k1 & 15) << 4;
		int i2 = k1 & 240;
		double d5 = (double)((float)l1 / 256.0F);
		double d6 = (double)(((float)l1 + 15.99F) / 256.0F);
		double d7 = (double)((float)i2 / 256.0F);
		double d8 = (double)(((float)i2 + 15.99F) / 256.0F);
		float f1 = 0.125F;
		float f2 = (float)(i + 1);
		float f3 = (float)(i + 1);
		float f4 = (float)(i + 0);
		float f5 = (float)(i + 0);
		float f6 = (float)(k + 0);
		float f7 = (float)(k + 1);
		float f8 = (float)(k + 1);
		float f9 = (float)(k + 0);
		float f10 = (float)j + f1;
		if(i1 == 2) {
			f2 = f3 = (float)(i + 0);
			f4 = f5 = (float)(i + 1);
			f6 = f9 = (float)(k + 1);
			f7 = f8 = (float)(k + 0);
		} else if(i1 == 3) {
			f2 = f5 = (float)(i + 0);
			f3 = f4 = (float)(i + 1);
			f6 = f7 = (float)(k + 0);
			f8 = f9 = (float)(k + 1);
		} else if(i1 == 1) {
			f2 = f5 = (float)(i + 1);
			f3 = f4 = (float)(i + 0);
			f6 = f7 = (float)(k + 1);
			f8 = f9 = (float)(k + 0);
		}

		tessellator.addVertexWithUV((double)f5, (double)f10, (double)f9, d5, d7);
		tessellator.addVertexWithUV((double)f4, (double)f10, (double)f8, d5, d8);
		tessellator.addVertexWithUV((double)f3, (double)f10, (double)f7, d6, d8);
		tessellator.addVertexWithUV((double)f2, (double)f10, (double)f6, d6, d7);
		return true;
	}

	public void func_31078_d(Block block, int i, int j, int k) {
		this.renderAllFaces = true;
		this.func_31074_b(block, i, j, k, true);
		this.renderAllFaces = false;
	}

	public boolean func_31074_b(Block block, int i, int j, int k, boolean flag) {
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		boolean flag1 = flag || (l & 8) != 0;
		int i1 = BlockPistonBase.func_31044_d(l);
		if(flag1) {
			switch(i1) {
			case 0:
				this.field_31087_g = 3;
				this.field_31086_h = 3;
				this.field_31085_i = 3;
				this.field_31084_j = 3;
				block.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 1:
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
				break;
			case 2:
				this.field_31085_i = 1;
				this.field_31084_j = 2;
				block.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;
			case 3:
				this.field_31085_i = 2;
				this.field_31084_j = 1;
				this.field_31083_k = 3;
				this.field_31082_l = 3;
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
				break;
			case 4:
				this.field_31087_g = 1;
				this.field_31086_h = 2;
				this.field_31083_k = 2;
				this.field_31082_l = 1;
				block.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 5:
				this.field_31087_g = 2;
				this.field_31086_h = 1;
				this.field_31083_k = 1;
				this.field_31082_l = 2;
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
			}

			this.renderStandardBlock(block, i, j, k);
			this.field_31087_g = 0;
			this.field_31086_h = 0;
			this.field_31085_i = 0;
			this.field_31084_j = 0;
			this.field_31083_k = 0;
			this.field_31082_l = 0;
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			switch(i1) {
			case 0:
				this.field_31087_g = 3;
				this.field_31086_h = 3;
				this.field_31085_i = 3;
				this.field_31084_j = 3;
			case 1:
			default:
				break;
			case 2:
				this.field_31085_i = 1;
				this.field_31084_j = 2;
				break;
			case 3:
				this.field_31085_i = 2;
				this.field_31084_j = 1;
				this.field_31083_k = 3;
				this.field_31082_l = 3;
				break;
			case 4:
				this.field_31087_g = 1;
				this.field_31086_h = 2;
				this.field_31083_k = 2;
				this.field_31082_l = 1;
				break;
			case 5:
				this.field_31087_g = 2;
				this.field_31086_h = 1;
				this.field_31083_k = 1;
				this.field_31082_l = 2;
			}

			this.renderStandardBlock(block, i, j, k);
			this.field_31087_g = 0;
			this.field_31086_h = 0;
			this.field_31085_i = 0;
			this.field_31084_j = 0;
			this.field_31083_k = 0;
			this.field_31082_l = 0;
		}

		return true;
	}

	public void func_31076_a(double d, double d1, double d2, double d3, double d4, double d5, float f, double d6) {
		int i = 108;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		Tessellator tessellator = Tessellator.instance;
		double d7 = (double)((float)(j + 0) / 256.0F);
		double d8 = (double)((float)(k + 0) / 256.0F);
		double d9 = ((double)j + d6 - 0.01D) / 256.0D;
		double d10 = ((double)((float)k + 4.0F) - 0.01D) / 256.0D;
		tessellator.setColorOpaque_F(f, f, f);
		tessellator.addVertexWithUV(d, d3, d4, d9, d8);
		tessellator.addVertexWithUV(d, d2, d4, d7, d8);
		tessellator.addVertexWithUV(d1, d2, d5, d7, d10);
		tessellator.addVertexWithUV(d1, d3, d5, d9, d10);
	}

	public void func_31081_b(double d, double d1, double d2, double d3, double d4, double d5, float f, double d6) {
		int i = 108;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		Tessellator tessellator = Tessellator.instance;
		double d7 = (double)((float)(j + 0) / 256.0F);
		double d8 = (double)((float)(k + 0) / 256.0F);
		double d9 = ((double)j + d6 - 0.01D) / 256.0D;
		double d10 = ((double)((float)k + 4.0F) - 0.01D) / 256.0D;
		tessellator.setColorOpaque_F(f, f, f);
		tessellator.addVertexWithUV(d, d2, d5, d9, d8);
		tessellator.addVertexWithUV(d, d2, d4, d7, d8);
		tessellator.addVertexWithUV(d1, d3, d4, d7, d10);
		tessellator.addVertexWithUV(d1, d3, d5, d9, d10);
	}

	public void func_31077_c(double d, double d1, double d2, double d3, double d4, double d5, float f, double d6) {
		int i = 108;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		Tessellator tessellator = Tessellator.instance;
		double d7 = (double)((float)(j + 0) / 256.0F);
		double d8 = (double)((float)(k + 0) / 256.0F);
		double d9 = ((double)j + d6 - 0.01D) / 256.0D;
		double d10 = ((double)((float)k + 4.0F) - 0.01D) / 256.0D;
		tessellator.setColorOpaque_F(f, f, f);
		tessellator.addVertexWithUV(d1, d2, d4, d9, d8);
		tessellator.addVertexWithUV(d, d2, d4, d7, d8);
		tessellator.addVertexWithUV(d, d3, d5, d7, d10);
		tessellator.addVertexWithUV(d1, d3, d5, d9, d10);
	}

	public void func_31079_a(Block block, int i, int j, int k, boolean flag) {
		this.renderAllFaces = true;
		this.func_31080_c(block, i, j, k, flag);
		this.renderAllFaces = false;
	}

	public boolean func_31080_c(Block block, int i, int j, int k, boolean flag) {
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		int i1 = BlockPistonExtension.func_31050_c(l);
		float f = block.getBlockBrightness(this.blockAccess, i, j, k);
		float f1 = flag ? 1.0F : 0.5F;
		double d = flag ? 16.0D : 8.0D;
		switch(i1) {
		case 0:
			this.field_31087_g = 3;
			this.field_31086_h = 3;
			this.field_31085_i = 3;
			this.field_31084_j = 3;
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			this.func_31076_a((double)((float)i + 0.375F), (double)((float)i + 0.625F), (double)((float)j + 0.25F), (double)((float)j + 0.25F + f1), (double)((float)k + 0.625F), (double)((float)k + 0.625F), f * 0.8F, d);
			this.func_31076_a((double)((float)i + 0.625F), (double)((float)i + 0.375F), (double)((float)j + 0.25F), (double)((float)j + 0.25F + f1), (double)((float)k + 0.375F), (double)((float)k + 0.375F), f * 0.8F, d);
			this.func_31076_a((double)((float)i + 0.375F), (double)((float)i + 0.375F), (double)((float)j + 0.25F), (double)((float)j + 0.25F + f1), (double)((float)k + 0.375F), (double)((float)k + 0.625F), f * 0.6F, d);
			this.func_31076_a((double)((float)i + 0.625F), (double)((float)i + 0.625F), (double)((float)j + 0.25F), (double)((float)j + 0.25F + f1), (double)((float)k + 0.625F), (double)((float)k + 0.375F), f * 0.6F, d);
			break;
		case 1:
			block.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			this.func_31076_a((double)((float)i + 0.375F), (double)((float)i + 0.625F), (double)((float)j - 0.25F + 1.0F - f1), (double)((float)j - 0.25F + 1.0F), (double)((float)k + 0.625F), (double)((float)k + 0.625F), f * 0.8F, d);
			this.func_31076_a((double)((float)i + 0.625F), (double)((float)i + 0.375F), (double)((float)j - 0.25F + 1.0F - f1), (double)((float)j - 0.25F + 1.0F), (double)((float)k + 0.375F), (double)((float)k + 0.375F), f * 0.8F, d);
			this.func_31076_a((double)((float)i + 0.375F), (double)((float)i + 0.375F), (double)((float)j - 0.25F + 1.0F - f1), (double)((float)j - 0.25F + 1.0F), (double)((float)k + 0.375F), (double)((float)k + 0.625F), f * 0.6F, d);
			this.func_31076_a((double)((float)i + 0.625F), (double)((float)i + 0.625F), (double)((float)j - 0.25F + 1.0F - f1), (double)((float)j - 0.25F + 1.0F), (double)((float)k + 0.625F), (double)((float)k + 0.375F), f * 0.6F, d);
			break;
		case 2:
			this.field_31085_i = 1;
			this.field_31084_j = 2;
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
			this.renderStandardBlock(block, i, j, k);
			this.func_31081_b((double)((float)i + 0.375F), (double)((float)i + 0.375F), (double)((float)j + 0.625F), (double)((float)j + 0.375F), (double)((float)k + 0.25F), (double)((float)k + 0.25F + f1), f * 0.6F, d);
			this.func_31081_b((double)((float)i + 0.625F), (double)((float)i + 0.625F), (double)((float)j + 0.375F), (double)((float)j + 0.625F), (double)((float)k + 0.25F), (double)((float)k + 0.25F + f1), f * 0.6F, d);
			this.func_31081_b((double)((float)i + 0.375F), (double)((float)i + 0.625F), (double)((float)j + 0.375F), (double)((float)j + 0.375F), (double)((float)k + 0.25F), (double)((float)k + 0.25F + f1), f * 0.5F, d);
			this.func_31081_b((double)((float)i + 0.625F), (double)((float)i + 0.375F), (double)((float)j + 0.625F), (double)((float)j + 0.625F), (double)((float)k + 0.25F), (double)((float)k + 0.25F + f1), f, d);
			break;
		case 3:
			this.field_31085_i = 2;
			this.field_31084_j = 1;
			this.field_31083_k = 3;
			this.field_31082_l = 3;
			block.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			this.func_31081_b((double)((float)i + 0.375F), (double)((float)i + 0.375F), (double)((float)j + 0.625F), (double)((float)j + 0.375F), (double)((float)k - 0.25F + 1.0F - f1), (double)((float)k - 0.25F + 1.0F), f * 0.6F, d);
			this.func_31081_b((double)((float)i + 0.625F), (double)((float)i + 0.625F), (double)((float)j + 0.375F), (double)((float)j + 0.625F), (double)((float)k - 0.25F + 1.0F - f1), (double)((float)k - 0.25F + 1.0F), f * 0.6F, d);
			this.func_31081_b((double)((float)i + 0.375F), (double)((float)i + 0.625F), (double)((float)j + 0.375F), (double)((float)j + 0.375F), (double)((float)k - 0.25F + 1.0F - f1), (double)((float)k - 0.25F + 1.0F), f * 0.5F, d);
			this.func_31081_b((double)((float)i + 0.625F), (double)((float)i + 0.375F), (double)((float)j + 0.625F), (double)((float)j + 0.625F), (double)((float)k - 0.25F + 1.0F - f1), (double)((float)k - 0.25F + 1.0F), f, d);
			break;
		case 4:
			this.field_31087_g = 1;
			this.field_31086_h = 2;
			this.field_31083_k = 2;
			this.field_31082_l = 1;
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			this.func_31077_c((double)((float)i + 0.25F), (double)((float)i + 0.25F + f1), (double)((float)j + 0.375F), (double)((float)j + 0.375F), (double)((float)k + 0.625F), (double)((float)k + 0.375F), f * 0.5F, d);
			this.func_31077_c((double)((float)i + 0.25F), (double)((float)i + 0.25F + f1), (double)((float)j + 0.625F), (double)((float)j + 0.625F), (double)((float)k + 0.375F), (double)((float)k + 0.625F), f, d);
			this.func_31077_c((double)((float)i + 0.25F), (double)((float)i + 0.25F + f1), (double)((float)j + 0.375F), (double)((float)j + 0.625F), (double)((float)k + 0.375F), (double)((float)k + 0.375F), f * 0.6F, d);
			this.func_31077_c((double)((float)i + 0.25F), (double)((float)i + 0.25F + f1), (double)((float)j + 0.625F), (double)((float)j + 0.375F), (double)((float)k + 0.625F), (double)((float)k + 0.625F), f * 0.6F, d);
			break;
		case 5:
			this.field_31087_g = 2;
			this.field_31086_h = 1;
			this.field_31083_k = 1;
			this.field_31082_l = 2;
			block.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			this.func_31077_c((double)((float)i - 0.25F + 1.0F - f1), (double)((float)i - 0.25F + 1.0F), (double)((float)j + 0.375F), (double)((float)j + 0.375F), (double)((float)k + 0.625F), (double)((float)k + 0.375F), f * 0.5F, d);
			this.func_31077_c((double)((float)i - 0.25F + 1.0F - f1), (double)((float)i - 0.25F + 1.0F), (double)((float)j + 0.625F), (double)((float)j + 0.625F), (double)((float)k + 0.375F), (double)((float)k + 0.625F), f, d);
			this.func_31077_c((double)((float)i - 0.25F + 1.0F - f1), (double)((float)i - 0.25F + 1.0F), (double)((float)j + 0.375F), (double)((float)j + 0.625F), (double)((float)k + 0.375F), (double)((float)k + 0.375F), f * 0.6F, d);
			this.func_31077_c((double)((float)i - 0.25F + 1.0F - f1), (double)((float)i - 0.25F + 1.0F), (double)((float)j + 0.625F), (double)((float)j + 0.375F), (double)((float)k + 0.625F), (double)((float)k + 0.625F), f * 0.6F, d);
		}

		this.field_31087_g = 0;
		this.field_31086_h = 0;
		this.field_31085_i = 0;
		this.field_31084_j = 0;
		this.field_31083_k = 0;
		this.field_31082_l = 0;
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockLever(Block block, int i, int j, int k) {
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		int i1 = l & 7;
		boolean flag = (l & 8) > 0;
		Tessellator tessellator = Tessellator.instance;
		boolean flag1 = this.overrideBlockTexture >= 0;
		if(!flag1) {
			this.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
		}

		float f = 0.25F;
		float f1 = 0.1875F;
		float f2 = 0.1875F;
		if(i1 == 5) {
			block.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
		} else if(i1 == 6) {
			block.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, f2, 0.5F + f1);
		} else if(i1 == 4) {
			block.setBlockBounds(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0F);
		} else if(i1 == 3) {
			block.setBlockBounds(0.5F - f1, 0.5F - f, 0.0F, 0.5F + f1, 0.5F + f, f2);
		} else if(i1 == 2) {
			block.setBlockBounds(1.0F - f2, 0.5F - f, 0.5F - f1, 1.0F, 0.5F + f, 0.5F + f1);
		} else if(i1 == 1) {
			block.setBlockBounds(0.0F, 0.5F - f, 0.5F - f1, f2, 0.5F + f, 0.5F + f1);
		}

		this.renderStandardBlock(block, i, j, k);
		if(!flag1) {
			this.overrideBlockTexture = -1;
		}

		float f3 = block.getBlockBrightness(this.blockAccess, i, j, k);
		if(Block.lightValue[block.blockID] > 0) {
			f3 = 1.0F;
		}

		tessellator.setColorOpaque_F(f3, f3, f3);
		int j1 = block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			j1 = this.overrideBlockTexture;
		}

		int k1 = (j1 & 15) << 4;
		int l1 = j1 & 240;
		float f4 = (float)k1 / 256.0F;
		float f5 = ((float)k1 + 15.99F) / 256.0F;
		float f6 = (float)l1 / 256.0F;
		float f7 = ((float)l1 + 15.99F) / 256.0F;
		Vec3D[] avec3d = new Vec3D[8];
		float f8 = 0.0625F;
		float f9 = 0.0625F;
		float f10 = 0.625F;
		avec3d[0] = Vec3D.createVector((double)(-f8), 0.0D, (double)(-f9));
		avec3d[1] = Vec3D.createVector((double)f8, 0.0D, (double)(-f9));
		avec3d[2] = Vec3D.createVector((double)f8, 0.0D, (double)f9);
		avec3d[3] = Vec3D.createVector((double)(-f8), 0.0D, (double)f9);
		avec3d[4] = Vec3D.createVector((double)(-f8), (double)f10, (double)(-f9));
		avec3d[5] = Vec3D.createVector((double)f8, (double)f10, (double)(-f9));
		avec3d[6] = Vec3D.createVector((double)f8, (double)f10, (double)f9);
		avec3d[7] = Vec3D.createVector((double)(-f8), (double)f10, (double)f9);

		for(int vec3d = 0; vec3d < 8; ++vec3d) {
			if(flag) {
				avec3d[vec3d].zCoord -= 0.0625D;
				avec3d[vec3d].rotateAroundX(0.6981317F);
			} else {
				avec3d[vec3d].zCoord += 0.0625D;
				avec3d[vec3d].rotateAroundX(-0.6981317F);
			}

			if(i1 == 6) {
				avec3d[vec3d].rotateAroundY(1.570796F);
			}

			if(i1 < 5) {
				avec3d[vec3d].yCoord -= 0.375D;
				avec3d[vec3d].rotateAroundX(1.570796F);
				if(i1 == 4) {
					avec3d[vec3d].rotateAroundY(0.0F);
				}

				if(i1 == 3) {
					avec3d[vec3d].rotateAroundY(3.141593F);
				}

				if(i1 == 2) {
					avec3d[vec3d].rotateAroundY(1.570796F);
				}

				if(i1 == 1) {
					avec3d[vec3d].rotateAroundY(-1.570796F);
				}

				avec3d[vec3d].xCoord += (double)i + 0.5D;
				avec3d[vec3d].yCoord += (double)((float)j + 0.5F);
				avec3d[vec3d].zCoord += (double)k + 0.5D;
			} else {
				avec3d[vec3d].xCoord += (double)i + 0.5D;
				avec3d[vec3d].yCoord += (double)((float)j + 0.125F);
				avec3d[vec3d].zCoord += (double)k + 0.5D;
			}
		}

		Vec3D vec3D30 = null;
		Vec3D vec3d1 = null;
		Vec3D vec3d2 = null;
		Vec3D vec3d3 = null;

		for(int j2 = 0; j2 < 6; ++j2) {
			if(j2 == 0) {
				f4 = (float)(k1 + 7) / 256.0F;
				f5 = ((float)(k1 + 9) - 0.01F) / 256.0F;
				f6 = (float)(l1 + 6) / 256.0F;
				f7 = ((float)(l1 + 8) - 0.01F) / 256.0F;
			} else if(j2 == 2) {
				f4 = (float)(k1 + 7) / 256.0F;
				f5 = ((float)(k1 + 9) - 0.01F) / 256.0F;
				f6 = (float)(l1 + 6) / 256.0F;
				f7 = ((float)(l1 + 16) - 0.01F) / 256.0F;
			}

			if(j2 == 0) {
				vec3D30 = avec3d[0];
				vec3d1 = avec3d[1];
				vec3d2 = avec3d[2];
				vec3d3 = avec3d[3];
			} else if(j2 == 1) {
				vec3D30 = avec3d[7];
				vec3d1 = avec3d[6];
				vec3d2 = avec3d[5];
				vec3d3 = avec3d[4];
			} else if(j2 == 2) {
				vec3D30 = avec3d[1];
				vec3d1 = avec3d[0];
				vec3d2 = avec3d[4];
				vec3d3 = avec3d[5];
			} else if(j2 == 3) {
				vec3D30 = avec3d[2];
				vec3d1 = avec3d[1];
				vec3d2 = avec3d[5];
				vec3d3 = avec3d[6];
			} else if(j2 == 4) {
				vec3D30 = avec3d[3];
				vec3d1 = avec3d[2];
				vec3d2 = avec3d[6];
				vec3d3 = avec3d[7];
			} else if(j2 == 5) {
				vec3D30 = avec3d[0];
				vec3d1 = avec3d[3];
				vec3d2 = avec3d[7];
				vec3d3 = avec3d[4];
			}

			tessellator.addVertexWithUV(vec3D30.xCoord, vec3D30.yCoord, vec3D30.zCoord, (double)f4, (double)f7);
			tessellator.addVertexWithUV(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, (double)f5, (double)f7);
			tessellator.addVertexWithUV(vec3d2.xCoord, vec3d2.yCoord, vec3d2.zCoord, (double)f5, (double)f6);
			tessellator.addVertexWithUV(vec3d3.xCoord, vec3d3.yCoord, vec3d3.zCoord, (double)f4, (double)f6);
		}

		return true;
	}

	public boolean renderBlockFire(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			l = this.overrideBlockTexture;
		}

		float f = block.getBlockBrightness(this.blockAccess, i, j, k);
		tessellator.setColorOpaque_F(f, f, f);
		int i1 = (l & 15) << 4;
		int j1 = l & 240;
		double d = (double)((float)i1 / 256.0F);
		double d1 = (double)(((float)i1 + 15.99F) / 256.0F);
		double d2 = (double)((float)j1 / 256.0F);
		double d3 = (double)(((float)j1 + 15.99F) / 256.0F);
		float f1 = 1.4F;
		double d7;
		double d9;
		double d11;
		double d13;
		double d15;
		double d17;
		double d19;
		if(!this.blockAccess.isBlockNormalCube(i, j - 1, k) && !Block.fire.canBlockCatchFire(this.blockAccess, i, j - 1, k)) {
			float f46 = 0.2F;
			float f3 = 0.0625F;
			if((i + j + k & 1) == 1) {
				d = (double)((float)i1 / 256.0F);
				d1 = (double)(((float)i1 + 15.99F) / 256.0F);
				d2 = (double)((float)(j1 + 16) / 256.0F);
				d3 = (double)(((float)j1 + 15.99F + 16.0F) / 256.0F);
			}

			if((i / 2 + j / 2 + k / 2 & 1) == 1) {
				d7 = d1;
				d1 = d;
				d = d7;
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i - 1, j, k)) {
				tessellator.addVertexWithUV((double)((float)i + f46), (double)((float)j + f1 + f3), (double)(k + 1), d1, d2);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 1), d1, d3);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.addVertexWithUV((double)((float)i + f46), (double)((float)j + f1 + f3), (double)(k + 0), d, d2);
				tessellator.addVertexWithUV((double)((float)i + f46), (double)((float)j + f1 + f3), (double)(k + 0), d, d2);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 1), d1, d3);
				tessellator.addVertexWithUV((double)((float)i + f46), (double)((float)j + f1 + f3), (double)(k + 1), d1, d2);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i + 1, j, k)) {
				tessellator.addVertexWithUV((double)((float)(i + 1) - f46), (double)((float)j + f1 + f3), (double)(k + 0), d, d2);
				tessellator.addVertexWithUV((double)(i + 1 - 0), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.addVertexWithUV((double)(i + 1 - 0), (double)((float)(j + 0) + f3), (double)(k + 1), d1, d3);
				tessellator.addVertexWithUV((double)((float)(i + 1) - f46), (double)((float)j + f1 + f3), (double)(k + 1), d1, d2);
				tessellator.addVertexWithUV((double)((float)(i + 1) - f46), (double)((float)j + f1 + f3), (double)(k + 1), d1, d2);
				tessellator.addVertexWithUV((double)(i + 1 - 0), (double)((float)(j + 0) + f3), (double)(k + 1), d1, d3);
				tessellator.addVertexWithUV((double)(i + 1 - 0), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.addVertexWithUV((double)((float)(i + 1) - f46), (double)((float)j + f1 + f3), (double)(k + 0), d, d2);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i, j, k - 1)) {
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f1 + f3), (double)((float)k + f46), d1, d2);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 0), d1, d3);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f1 + f3), (double)((float)k + f46), d, d2);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f1 + f3), (double)((float)k + f46), d, d2);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 0), d1, d3);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f1 + f3), (double)((float)k + f46), d1, d2);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i, j, k + 1)) {
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f1 + f3), (double)((float)(k + 1) - f46), d, d2);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)(j + 0) + f3), (double)(k + 1 - 0), d, d3);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 1 - 0), d1, d3);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f1 + f3), (double)((float)(k + 1) - f46), d1, d2);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f1 + f3), (double)((float)(k + 1) - f46), d1, d2);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 1 - 0), d1, d3);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)(j + 0) + f3), (double)(k + 1 - 0), d, d3);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f1 + f3), (double)((float)(k + 1) - f46), d, d2);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i, j + 1, k)) {
				d7 = (double)i + 0.5D + 0.5D;
				d9 = (double)i + 0.5D - 0.5D;
				d11 = (double)k + 0.5D + 0.5D;
				d13 = (double)k + 0.5D - 0.5D;
				d15 = (double)i + 0.5D - 0.5D;
				d17 = (double)i + 0.5D + 0.5D;
				d19 = (double)k + 0.5D - 0.5D;
				double d20 = (double)k + 0.5D + 0.5D;
				double d21 = (double)((float)i1 / 256.0F);
				double d22 = (double)(((float)i1 + 15.99F) / 256.0F);
				double d23 = (double)((float)j1 / 256.0F);
				double d24 = (double)(((float)j1 + 15.99F) / 256.0F);
				++j;
				float f4 = -0.2F;
				if((i + j + k & 1) == 0) {
					tessellator.addVertexWithUV(d15, (double)((float)j + f4), (double)(k + 0), d22, d23);
					tessellator.addVertexWithUV(d7, (double)(j + 0), (double)(k + 0), d22, d24);
					tessellator.addVertexWithUV(d7, (double)(j + 0), (double)(k + 1), d21, d24);
					tessellator.addVertexWithUV(d15, (double)((float)j + f4), (double)(k + 1), d21, d23);
					d21 = (double)((float)i1 / 256.0F);
					d22 = (double)(((float)i1 + 15.99F) / 256.0F);
					d23 = (double)((float)(j1 + 16) / 256.0F);
					d24 = (double)(((float)j1 + 15.99F + 16.0F) / 256.0F);
					tessellator.addVertexWithUV(d17, (double)((float)j + f4), (double)(k + 1), d22, d23);
					tessellator.addVertexWithUV(d9, (double)(j + 0), (double)(k + 1), d22, d24);
					tessellator.addVertexWithUV(d9, (double)(j + 0), (double)(k + 0), d21, d24);
					tessellator.addVertexWithUV(d17, (double)((float)j + f4), (double)(k + 0), d21, d23);
				} else {
					tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f4), d20, d22, d23);
					tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), d13, d22, d24);
					tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), d13, d21, d24);
					tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f4), d20, d21, d23);
					d21 = (double)((float)i1 / 256.0F);
					d22 = (double)(((float)i1 + 15.99F) / 256.0F);
					d23 = (double)((float)(j1 + 16) / 256.0F);
					d24 = (double)(((float)j1 + 15.99F + 16.0F) / 256.0F);
					tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f4), d19, d22, d23);
					tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), d11, d22, d24);
					tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), d11, d21, d24);
					tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f4), d19, d21, d23);
				}
			}
		} else {
			double f2 = (double)i + 0.5D + 0.2D;
			d7 = (double)i + 0.5D - 0.2D;
			d9 = (double)k + 0.5D + 0.2D;
			d11 = (double)k + 0.5D - 0.2D;
			d13 = (double)i + 0.5D - 0.3D;
			d15 = (double)i + 0.5D + 0.3D;
			d17 = (double)k + 0.5D - 0.3D;
			d19 = (double)k + 0.5D + 0.3D;
			tessellator.addVertexWithUV(d13, (double)((float)j + f1), (double)(k + 1), d1, d2);
			tessellator.addVertexWithUV(f2, (double)(j + 0), (double)(k + 1), d1, d3);
			tessellator.addVertexWithUV(f2, (double)(j + 0), (double)(k + 0), d, d3);
			tessellator.addVertexWithUV(d13, (double)((float)j + f1), (double)(k + 0), d, d2);
			tessellator.addVertexWithUV(d15, (double)((float)j + f1), (double)(k + 0), d1, d2);
			tessellator.addVertexWithUV(d7, (double)(j + 0), (double)(k + 0), d1, d3);
			tessellator.addVertexWithUV(d7, (double)(j + 0), (double)(k + 1), d, d3);
			tessellator.addVertexWithUV(d15, (double)((float)j + f1), (double)(k + 1), d, d2);
			d = (double)((float)i1 / 256.0F);
			d1 = (double)(((float)i1 + 15.99F) / 256.0F);
			d2 = (double)((float)(j1 + 16) / 256.0F);
			d3 = (double)(((float)j1 + 15.99F + 16.0F) / 256.0F);
			tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f1), d19, d1, d2);
			tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), d11, d1, d3);
			tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), d11, d, d3);
			tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f1), d19, d, d2);
			tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f1), d17, d1, d2);
			tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), d9, d1, d3);
			tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), d9, d, d3);
			tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f1), d17, d, d2);
			f2 = (double)i + 0.5D - 0.5D;
			d7 = (double)i + 0.5D + 0.5D;
			d9 = (double)k + 0.5D - 0.5D;
			d11 = (double)k + 0.5D + 0.5D;
			d13 = (double)i + 0.5D - 0.4D;
			d15 = (double)i + 0.5D + 0.4D;
			d17 = (double)k + 0.5D - 0.4D;
			d19 = (double)k + 0.5D + 0.4D;
			tessellator.addVertexWithUV(d13, (double)((float)j + f1), (double)(k + 0), d, d2);
			tessellator.addVertexWithUV(f2, (double)(j + 0), (double)(k + 0), d, d3);
			tessellator.addVertexWithUV(f2, (double)(j + 0), (double)(k + 1), d1, d3);
			tessellator.addVertexWithUV(d13, (double)((float)j + f1), (double)(k + 1), d1, d2);
			tessellator.addVertexWithUV(d15, (double)((float)j + f1), (double)(k + 1), d, d2);
			tessellator.addVertexWithUV(d7, (double)(j + 0), (double)(k + 1), d, d3);
			tessellator.addVertexWithUV(d7, (double)(j + 0), (double)(k + 0), d1, d3);
			tessellator.addVertexWithUV(d15, (double)((float)j + f1), (double)(k + 0), d1, d2);
			d = (double)((float)i1 / 256.0F);
			d1 = (double)(((float)i1 + 15.99F) / 256.0F);
			d2 = (double)((float)j1 / 256.0F);
			d3 = (double)(((float)j1 + 15.99F) / 256.0F);
			tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f1), d19, d, d2);
			tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), d11, d, d3);
			tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), d11, d1, d3);
			tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f1), d19, d1, d2);
			tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f1), d17, d, d2);
			tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), d9, d, d3);
			tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), d9, d1, d3);
			tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f1), d17, d1, d2);
		}

		return true;
	}

	public static void setRedstoneColors(float[][] af) {
		if(af.length != 16) {
			throw new IllegalArgumentException("Must be 16 colors.");
		} else {
			for(int i = 0; i < af.length; ++i) {
				if(af[i].length != 3) {
					throw new IllegalArgumentException("Must be 3 channels in a color.");
				}
			}

			redstoneColors = af;
		}
	}

	public boolean renderBlockRedstoneWire(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		int i1 = block.getBlockTextureFromSideAndMetadata(1, l);
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		float f = block.getBlockBrightness(this.blockAccess, i, j, k);
		float[] af = redstoneColors[l];
		float f1 = af[0];
		float f2 = af[1];
		float f3 = af[2];
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		double d = (double)((float)j1 / 256.0F);
		double d1 = (double)(((float)j1 + 15.99F) / 256.0F);
		double d2 = (double)((float)k1 / 256.0F);
		double d3 = (double)(((float)k1 + 15.99F) / 256.0F);
		boolean flag = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i - 1, j, k, 1) || !this.blockAccess.isBlockNormalCube(i - 1, j, k) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i - 1, j - 1, k, -1);
		boolean flag1 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i + 1, j, k, 3) || !this.blockAccess.isBlockNormalCube(i + 1, j, k) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i + 1, j - 1, k, -1);
		boolean flag2 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i, j, k - 1, 2) || !this.blockAccess.isBlockNormalCube(i, j, k - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i, j - 1, k - 1, -1);
		boolean flag3 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i, j, k + 1, 0) || !this.blockAccess.isBlockNormalCube(i, j, k + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i, j - 1, k + 1, -1);
		if(!this.blockAccess.isBlockNormalCube(i, j + 1, k)) {
			if(this.blockAccess.isBlockNormalCube(i - 1, j, k) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i - 1, j + 1, k, -1)) {
				flag = true;
			}

			if(this.blockAccess.isBlockNormalCube(i + 1, j, k) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i + 1, j + 1, k, -1)) {
				flag1 = true;
			}

			if(this.blockAccess.isBlockNormalCube(i, j, k - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i, j + 1, k - 1, -1)) {
				flag2 = true;
			}

			if(this.blockAccess.isBlockNormalCube(i, j, k + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i, j + 1, k + 1, -1)) {
				flag3 = true;
			}
		}

		float f4 = (float)(i + 0);
		float f5 = (float)(i + 1);
		float f6 = (float)(k + 0);
		float f7 = (float)(k + 1);
		byte byte0 = 0;
		if((flag || flag1) && !flag2 && !flag3) {
			byte0 = 1;
		}

		if((flag2 || flag3) && !flag1 && !flag) {
			byte0 = 2;
		}

		if(byte0 != 0) {
			d = (double)((float)(j1 + 16) / 256.0F);
			d1 = (double)(((float)(j1 + 16) + 15.99F) / 256.0F);
			d2 = (double)((float)k1 / 256.0F);
			d3 = (double)(((float)k1 + 15.99F) / 256.0F);
		}

		if(byte0 == 0) {
			if(flag1 || flag2 || flag3 || flag) {
				if(!flag) {
					f4 += 0.3125F;
				}

				if(!flag) {
					d += 0.01953125D;
				}

				if(!flag1) {
					f5 -= 0.3125F;
				}

				if(!flag1) {
					d1 -= 0.01953125D;
				}

				if(!flag2) {
					f6 += 0.3125F;
				}

				if(!flag2) {
					d2 += 0.01953125D;
				}

				if(!flag3) {
					f7 -= 0.3125F;
				}

				if(!flag3) {
					d3 -= 0.01953125D;
				}
			}

			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f7, d1, d3);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f6, d1, d2);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f6, d, d2);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f7, d, d3);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f7, d1, d3 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f6, d1, d2 + 0.0625D);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f6, d, d2 + 0.0625D);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f7, d, d3 + 0.0625D);
		} else if(byte0 == 1) {
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f7, d1, d3);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f6, d1, d2);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f6, d, d2);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f7, d, d3);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f7, d1, d3 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f6, d1, d2 + 0.0625D);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f6, d, d2 + 0.0625D);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f7, d, d3 + 0.0625D);
		} else if(byte0 == 2) {
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f7, d1, d3);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f6, d, d3);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f6, d, d2);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f7, d1, d2);
			tessellator.setColorOpaque_F(f, f, f);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f7, d1, d3 + 0.0625D);
			tessellator.addVertexWithUV((double)f5, (double)((float)j + 0.015625F), (double)f6, d, d3 + 0.0625D);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f6, d, d2 + 0.0625D);
			tessellator.addVertexWithUV((double)f4, (double)((float)j + 0.015625F), (double)f7, d1, d2 + 0.0625D);
		}

		if(!this.blockAccess.isBlockNormalCube(i, j + 1, k)) {
			double d4 = (double)((float)(j1 + 16) / 256.0F);
			double d5 = (double)(((float)(j1 + 16) + 15.99F) / 256.0F);
			double d6 = (double)((float)k1 / 256.0F);
			double d7 = (double)(((float)k1 + 15.99F) / 256.0F);
			if(this.blockAccess.isBlockNormalCube(i - 1, j, k) && this.blockAccess.getBlockId(i - 1, j + 1, k) == Block.redstoneWire.blockID) {
				tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
				tessellator.addVertexWithUV((double)((float)i + 0.015625F), (double)((float)(j + 1) + 0.021875F), (double)(k + 1), d5, d6);
				tessellator.addVertexWithUV((double)((float)i + 0.015625F), (double)(j + 0), (double)(k + 1), d4, d6);
				tessellator.addVertexWithUV((double)((float)i + 0.015625F), (double)(j + 0), (double)(k + 0), d4, d7);
				tessellator.addVertexWithUV((double)((float)i + 0.015625F), (double)((float)(j + 1) + 0.021875F), (double)(k + 0), d5, d7);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV((double)((float)i + 0.015625F), (double)((float)(j + 1) + 0.021875F), (double)(k + 1), d5, d6 + 0.0625D);
				tessellator.addVertexWithUV((double)((float)i + 0.015625F), (double)(j + 0), (double)(k + 1), d4, d6 + 0.0625D);
				tessellator.addVertexWithUV((double)((float)i + 0.015625F), (double)(j + 0), (double)(k + 0), d4, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)((float)i + 0.015625F), (double)((float)(j + 1) + 0.021875F), (double)(k + 0), d5, d7 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(i + 1, j, k) && this.blockAccess.getBlockId(i + 1, j + 1, k) == Block.redstoneWire.blockID) {
				tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
				tessellator.addVertexWithUV((double)((float)(i + 1) - 0.015625F), (double)(j + 0), (double)(k + 1), d4, d7);
				tessellator.addVertexWithUV((double)((float)(i + 1) - 0.015625F), (double)((float)(j + 1) + 0.021875F), (double)(k + 1), d5, d7);
				tessellator.addVertexWithUV((double)((float)(i + 1) - 0.015625F), (double)((float)(j + 1) + 0.021875F), (double)(k + 0), d5, d6);
				tessellator.addVertexWithUV((double)((float)(i + 1) - 0.015625F), (double)(j + 0), (double)(k + 0), d4, d6);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV((double)((float)(i + 1) - 0.015625F), (double)(j + 0), (double)(k + 1), d4, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)((float)(i + 1) - 0.015625F), (double)((float)(j + 1) + 0.021875F), (double)(k + 1), d5, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)((float)(i + 1) - 0.015625F), (double)((float)(j + 1) + 0.021875F), (double)(k + 0), d5, d6 + 0.0625D);
				tessellator.addVertexWithUV((double)((float)(i + 1) - 0.015625F), (double)(j + 0), (double)(k + 0), d4, d6 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(i, j, k - 1) && this.blockAccess.getBlockId(i, j + 1, k - 1) == Block.redstoneWire.blockID) {
				tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
				tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), (double)((float)k + 0.015625F), d4, d7);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)(j + 1) + 0.021875F), (double)((float)k + 0.015625F), d5, d7);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 1) + 0.021875F), (double)((float)k + 0.015625F), d5, d6);
				tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), (double)((float)k + 0.015625F), d4, d6);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), (double)((float)k + 0.015625F), d4, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)(j + 1) + 0.021875F), (double)((float)k + 0.015625F), d5, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 1) + 0.021875F), (double)((float)k + 0.015625F), d5, d6 + 0.0625D);
				tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), (double)((float)k + 0.015625F), d4, d6 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(i, j, k + 1) && this.blockAccess.getBlockId(i, j + 1, k + 1) == Block.redstoneWire.blockID) {
				tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)(j + 1) + 0.021875F), (double)((float)(k + 1) - 0.015625F), d5, d6);
				tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), (double)((float)(k + 1) - 0.015625F), d4, d6);
				tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), (double)((float)(k + 1) - 0.015625F), d4, d7);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 1) + 0.021875F), (double)((float)(k + 1) - 0.015625F), d5, d7);
				tessellator.setColorOpaque_F(f, f, f);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)(j + 1) + 0.021875F), (double)((float)(k + 1) - 0.015625F), d5, d6 + 0.0625D);
				tessellator.addVertexWithUV((double)(i + 1), (double)(j + 0), (double)((float)(k + 1) - 0.015625F), d4, d6 + 0.0625D);
				tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), (double)((float)(k + 1) - 0.015625F), d4, d7 + 0.0625D);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)(j + 1) + 0.021875F), (double)((float)(k + 1) - 0.015625F), d5, d7 + 0.0625D);
			}
		}

		return true;
	}

	public boolean renderBlockMinecartTrack(BlockRail blockrail, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		int i1 = blockrail.getBlockTextureFromSideAndMetadata(0, l);
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		if(blockrail.getIsPowered()) {
			l &= 7;
		}

		float f = blockrail.getBlockBrightness(this.blockAccess, i, j, k);
		tessellator.setColorOpaque_F(f, f, f);
		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		double d = (double)((float)j1 / 256.0F);
		double d1 = (double)(((float)j1 + 15.99F) / 256.0F);
		double d2 = (double)((float)k1 / 256.0F);
		double d3 = (double)(((float)k1 + 15.99F) / 256.0F);
		float f1 = 0.0625F;
		float f2 = (float)(i + 1);
		float f3 = (float)(i + 1);
		float f4 = (float)(i + 0);
		float f5 = (float)(i + 0);
		float f6 = (float)(k + 0);
		float f7 = (float)(k + 1);
		float f8 = (float)(k + 1);
		float f9 = (float)(k + 0);
		float f10 = (float)j + f1;
		float f11 = (float)j + f1;
		float f12 = (float)j + f1;
		float f13 = (float)j + f1;
		if(l != 1 && l != 2 && l != 3 && l != 7) {
			if(l == 8) {
				f2 = f3 = (float)(i + 0);
				f4 = f5 = (float)(i + 1);
				f6 = f9 = (float)(k + 1);
				f7 = f8 = (float)(k + 0);
			} else if(l == 9) {
				f2 = f5 = (float)(i + 0);
				f3 = f4 = (float)(i + 1);
				f6 = f7 = (float)(k + 0);
				f8 = f9 = (float)(k + 1);
			}
		} else {
			f2 = f5 = (float)(i + 1);
			f3 = f4 = (float)(i + 0);
			f6 = f7 = (float)(k + 1);
			f8 = f9 = (float)(k + 0);
		}

		if(l != 2 && l != 4) {
			if(l == 3 || l == 5) {
				++f11;
				++f12;
			}
		} else {
			++f10;
			++f13;
		}

		tessellator.addVertexWithUV((double)f2, (double)f10, (double)f6, d1, d2);
		tessellator.addVertexWithUV((double)f3, (double)f11, (double)f7, d1, d3);
		tessellator.addVertexWithUV((double)f4, (double)f12, (double)f8, d, d3);
		tessellator.addVertexWithUV((double)f5, (double)f13, (double)f9, d, d2);
		tessellator.addVertexWithUV((double)f5, (double)f13, (double)f9, d, d2);
		tessellator.addVertexWithUV((double)f4, (double)f12, (double)f8, d, d3);
		tessellator.addVertexWithUV((double)f3, (double)f11, (double)f7, d1, d3);
		tessellator.addVertexWithUV((double)f2, (double)f10, (double)f6, d1, d2);
		return true;
	}

	public boolean renderBlockLadder(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			l = this.overrideBlockTexture;
		}

		float f = block.getBlockBrightness(this.blockAccess, i, j, k);
		tessellator.setColorOpaque_F(f, f, f);
		int i1 = (l & 15) << 4;
		int j1 = l & 240;
		double d = (double)((float)i1 / 256.0F);
		double d1 = (double)(((float)i1 + 15.99F) / 256.0F);
		double d2 = (double)((float)j1 / 256.0F);
		double d3 = (double)(((float)j1 + 15.99F) / 256.0F);
		int k1 = this.blockAccess.getBlockMetadata(i, j, k);
		float f1 = 0.0F;
		float f2 = 0.05F;
		if(k1 == 5) {
			tessellator.addVertexWithUV((double)((float)i + f2), (double)((float)(j + 1) + f1), (double)((float)(k + 1) + f1), d, d2);
			tessellator.addVertexWithUV((double)((float)i + f2), (double)((float)(j + 0) - f1), (double)((float)(k + 1) + f1), d, d3);
			tessellator.addVertexWithUV((double)((float)i + f2), (double)((float)(j + 0) - f1), (double)((float)(k + 0) - f1), d1, d3);
			tessellator.addVertexWithUV((double)((float)i + f2), (double)((float)(j + 1) + f1), (double)((float)(k + 0) - f1), d1, d2);
		}

		if(k1 == 4) {
			tessellator.addVertexWithUV((double)((float)(i + 1) - f2), (double)((float)(j + 0) - f1), (double)((float)(k + 1) + f1), d1, d3);
			tessellator.addVertexWithUV((double)((float)(i + 1) - f2), (double)((float)(j + 1) + f1), (double)((float)(k + 1) + f1), d1, d2);
			tessellator.addVertexWithUV((double)((float)(i + 1) - f2), (double)((float)(j + 1) + f1), (double)((float)(k + 0) - f1), d, d2);
			tessellator.addVertexWithUV((double)((float)(i + 1) - f2), (double)((float)(j + 0) - f1), (double)((float)(k + 0) - f1), d, d3);
		}

		if(k1 == 3) {
			tessellator.addVertexWithUV((double)((float)(i + 1) + f1), (double)((float)(j + 0) - f1), (double)((float)k + f2), d1, d3);
			tessellator.addVertexWithUV((double)((float)(i + 1) + f1), (double)((float)(j + 1) + f1), (double)((float)k + f2), d1, d2);
			tessellator.addVertexWithUV((double)((float)(i + 0) - f1), (double)((float)(j + 1) + f1), (double)((float)k + f2), d, d2);
			tessellator.addVertexWithUV((double)((float)(i + 0) - f1), (double)((float)(j + 0) - f1), (double)((float)k + f2), d, d3);
		}

		if(k1 == 2) {
			tessellator.addVertexWithUV((double)((float)(i + 1) + f1), (double)((float)(j + 1) + f1), (double)((float)(k + 1) - f2), d, d2);
			tessellator.addVertexWithUV((double)((float)(i + 1) + f1), (double)((float)(j + 0) - f1), (double)((float)(k + 1) - f2), d, d3);
			tessellator.addVertexWithUV((double)((float)(i + 0) - f1), (double)((float)(j + 0) - f1), (double)((float)(k + 1) - f2), d1, d3);
			tessellator.addVertexWithUV((double)((float)(i + 0) - f1), (double)((float)(j + 1) + f1), (double)((float)(k + 1) - f2), d1, d2);
		}

		return true;
	}

	public boolean renderBlockReed(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(this.blockAccess, i, j, k);
		int l = block.colorMultiplier(this.blockAccess, i, j, k);
		float f1 = (float)(l >> 16 & 255) / 255.0F;
		float f2 = (float)(l >> 8 & 255) / 255.0F;
		float f3 = (float)(l & 255) / 255.0F;
		if(EntityRenderer.field_28135_a) {
			float d = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float d1 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = d;
			f2 = f5;
			f3 = d1;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		double d1 = (double)i;
		double d11 = (double)j;
		double d2 = (double)k;
		if(block == Block.tallGrass) {
			long l1 = (long)(i * 3129871) ^ (long)k * 116129781L ^ (long)j;
			l1 = l1 * l1 * 42317861L + l1 * 11L;
			d1 += ((double)((float)(l1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
			d11 += ((double)((float)(l1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
			d2 += ((double)((float)(l1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
		}

		this.renderCrossedSquares(block, this.blockAccess.getBlockMetadata(i, j, k), d1, d11, d2);
		return true;
	}

	public boolean renderBlockCrops(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(this.blockAccess, i, j, k);
		tessellator.setColorOpaque_F(f, f, f);
		this.func_1245_b(block, this.blockAccess.getBlockMetadata(i, j, k), (double)i, (double)j - 0.0625D, (double)k);
		return true;
	}

	public void renderTorchAtAngle(Block block, double d, double d1, double d2, double d3, double d4) {
		Tessellator tessellator = Tessellator.instance;
		int i = block.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		float f = (float)j / 256.0F;
		float f1 = ((float)j + 15.99F) / 256.0F;
		float f2 = (float)k / 256.0F;
		float f3 = ((float)k + 15.99F) / 256.0F;
		double d5 = (double)f + 7.0D / 256D;
		double d6 = (double)f2 + 6.0D / 256D;
		double d7 = (double)f + 9.0D / 256D;
		double d8 = (double)f2 + 8.0D / 256D;
		d += 0.5D;
		d2 += 0.5D;
		double d9 = d - 0.5D;
		double d10 = d + 0.5D;
		double d11 = d2 - 0.5D;
		double d12 = d2 + 0.5D;
		double d13 = 0.0625D;
		double d14 = 0.625D;
		tessellator.addVertexWithUV(d + d3 * (1.0D - d14) - d13, d1 + d14, d2 + d4 * (1.0D - d14) - d13, d5, d6);
		tessellator.addVertexWithUV(d + d3 * (1.0D - d14) - d13, d1 + d14, d2 + d4 * (1.0D - d14) + d13, d5, d8);
		tessellator.addVertexWithUV(d + d3 * (1.0D - d14) + d13, d1 + d14, d2 + d4 * (1.0D - d14) + d13, d7, d8);
		tessellator.addVertexWithUV(d + d3 * (1.0D - d14) + d13, d1 + d14, d2 + d4 * (1.0D - d14) - d13, d7, d6);
		tessellator.addVertexWithUV(d - d13, d1 + 1.0D, d11, (double)f, (double)f2);
		tessellator.addVertexWithUV(d - d13 + d3, d1 + 0.0D, d11 + d4, (double)f, (double)f3);
		tessellator.addVertexWithUV(d - d13 + d3, d1 + 0.0D, d12 + d4, (double)f1, (double)f3);
		tessellator.addVertexWithUV(d - d13, d1 + 1.0D, d12, (double)f1, (double)f2);
		tessellator.addVertexWithUV(d + d13, d1 + 1.0D, d12, (double)f, (double)f2);
		tessellator.addVertexWithUV(d + d3 + d13, d1 + 0.0D, d12 + d4, (double)f, (double)f3);
		tessellator.addVertexWithUV(d + d3 + d13, d1 + 0.0D, d11 + d4, (double)f1, (double)f3);
		tessellator.addVertexWithUV(d + d13, d1 + 1.0D, d11, (double)f1, (double)f2);
		tessellator.addVertexWithUV(d9, d1 + 1.0D, d2 + d13, (double)f, (double)f2);
		tessellator.addVertexWithUV(d9 + d3, d1 + 0.0D, d2 + d13 + d4, (double)f, (double)f3);
		tessellator.addVertexWithUV(d10 + d3, d1 + 0.0D, d2 + d13 + d4, (double)f1, (double)f3);
		tessellator.addVertexWithUV(d10, d1 + 1.0D, d2 + d13, (double)f1, (double)f2);
		tessellator.addVertexWithUV(d10, d1 + 1.0D, d2 - d13, (double)f, (double)f2);
		tessellator.addVertexWithUV(d10 + d3, d1 + 0.0D, d2 - d13 + d4, (double)f, (double)f3);
		tessellator.addVertexWithUV(d9 + d3, d1 + 0.0D, d2 - d13 + d4, (double)f1, (double)f3);
		tessellator.addVertexWithUV(d9, d1 + 1.0D, d2 - d13, (double)f1, (double)f2);
	}

	public void renderCrossedSquares(Block block, int i, double d, double d1, double d2) {
		Tessellator tessellator = Tessellator.instance;
		int j = block.getBlockTextureFromSideAndMetadata(0, i);
		if(this.overrideBlockTexture >= 0) {
			j = this.overrideBlockTexture;
		}

		int k = (j & 15) << 4;
		int l = j & 240;
		double d3 = (double)((float)k / 256.0F);
		double d4 = (double)(((float)k + 15.99F) / 256.0F);
		double d5 = (double)((float)l / 256.0F);
		double d6 = (double)(((float)l + 15.99F) / 256.0F);
		double d7 = d + 0.5D - (double)0.45F;
		double d8 = d + 0.5D + (double)0.45F;
		double d9 = d2 + 0.5D - (double)0.45F;
		double d10 = d2 + 0.5D + (double)0.45F;
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d4, d5);
	}

	public void func_1245_b(Block block, int i, double d, double d1, double d2) {
		Tessellator tessellator = Tessellator.instance;
		int j = block.getBlockTextureFromSideAndMetadata(0, i);
		if(this.overrideBlockTexture >= 0) {
			j = this.overrideBlockTexture;
		}

		int k = (j & 15) << 4;
		int l = j & 240;
		double d3 = (double)((float)k / 256.0F);
		double d4 = (double)(((float)k + 15.99F) / 256.0F);
		double d5 = (double)((float)l / 256.0F);
		double d6 = (double)(((float)l + 15.99F) / 256.0F);
		double d7 = d + 0.5D - 0.25D;
		double d8 = d + 0.5D + 0.25D;
		double d9 = d2 + 0.5D - 0.5D;
		double d10 = d2 + 0.5D + 0.5D;
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d4, d5);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d4, d5);
		d7 = d + 0.5D - 0.5D;
		d8 = d + 0.5D + 0.5D;
		d9 = d2 + 0.5D - 0.25D;
		d10 = d2 + 0.5D + 0.25D;
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d4, d5);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d4, d5);
	}

	public boolean renderBlockFluids(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		int l = block.colorMultiplier(this.blockAccess, i, j, k);
		float f = (float)(l >> 16 & 255) / 255.0F;
		float f1 = (float)(l >> 8 & 255) / 255.0F;
		float f2 = (float)(l & 255) / 255.0F;
		boolean flag = block.shouldSideBeRendered(this.blockAccess, i, j + 1, k, 1);
		boolean flag1 = block.shouldSideBeRendered(this.blockAccess, i, j - 1, k, 0);
		boolean[] aflag = new boolean[]{block.shouldSideBeRendered(this.blockAccess, i, j, k - 1, 2), block.shouldSideBeRendered(this.blockAccess, i, j, k + 1, 3), block.shouldSideBeRendered(this.blockAccess, i - 1, j, k, 4), block.shouldSideBeRendered(this.blockAccess, i + 1, j, k, 5)};
		if(!flag && !flag1 && !aflag[0] && !aflag[1] && !aflag[2] && !aflag[3]) {
			return false;
		} else {
			boolean flag2 = false;
			float f3 = 0.5F;
			float f4 = 1.0F;
			float f5 = 0.8F;
			float f6 = 0.6F;
			double d = 0.0D;
			double d1 = 1.0D;
			Material material = block.blockMaterial;
			int i1 = this.blockAccess.getBlockMetadata(i, j, k);
			float f7 = this.func_1224_a(i, j, k, material);
			float f8 = this.func_1224_a(i, j, k + 1, material);
			float f9 = this.func_1224_a(i + 1, j, k + 1, material);
			float f10 = this.func_1224_a(i + 1, j, k, material);
			int k1;
			int l2;
			float f15;
			float f17;
			float f19;
			if(this.renderAllFaces || flag) {
				flag2 = true;
				k1 = block.getBlockTextureFromSideAndMetadata(1, i1);
				float l1 = (float)BlockFluid.func_293_a(this.blockAccess, i, j, k, material);
				if(l1 > -999.0F) {
					k1 = block.getBlockTextureFromSideAndMetadata(2, i1);
				}

				int j2 = (k1 & 15) << 4;
				l2 = k1 & 240;
				double i3 = ((double)j2 + 8.0D) / 256.0D;
				double k3 = ((double)l2 + 8.0D) / 256.0D;
				if(l1 < -999.0F) {
					l1 = 0.0F;
				} else {
					i3 = (double)((float)(j2 + 16) / 256.0F);
					k3 = (double)((float)(l2 + 16) / 256.0F);
				}

				f15 = MathHelper.sin(l1) * 8.0F / 256.0F;
				f17 = MathHelper.cos(l1) * 8.0F / 256.0F;
				f19 = block.getBlockBrightness(this.blockAccess, i, j, k);
				tessellator.setColorOpaque_F(f4 * f19 * f, f4 * f19 * f1, f4 * f19 * f2);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f7), (double)(k + 0), i3 - (double)f17 - (double)f15, k3 - (double)f17 + (double)f15);
				tessellator.addVertexWithUV((double)(i + 0), (double)((float)j + f8), (double)(k + 1), i3 - (double)f17 + (double)f15, k3 + (double)f17 + (double)f15);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f9), (double)(k + 1), i3 + (double)f17 + (double)f15, k3 + (double)f17 - (double)f15);
				tessellator.addVertexWithUV((double)(i + 1), (double)((float)j + f10), (double)(k + 0), i3 + (double)f17 - (double)f15, k3 - (double)f17 - (double)f15);
			}

			if(this.renderAllFaces || flag1) {
				float f52 = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
				tessellator.setColorOpaque_F(f3 * f52, f3 * f52, f3 * f52);
				this.renderBottomFace(block, (double)i, (double)j, (double)k, block.getBlockTextureFromSide(0));
				flag2 = true;
			}

			for(k1 = 0; k1 < 4; ++k1) {
				int i53 = i;
				l2 = k;
				if(k1 == 0) {
					l2 = k - 1;
				}

				if(k1 == 1) {
					++l2;
				}

				if(k1 == 2) {
					i53 = i - 1;
				}

				if(k1 == 3) {
					++i53;
				}

				int i54 = block.getBlockTextureFromSideAndMetadata(k1 + 2, i1);
				int j3 = (i54 & 15) << 4;
				int i55 = i54 & 240;
				if(this.renderAllFaces || aflag[k1]) {
					float f13;
					float f20;
					float f21;
					if(k1 == 0) {
						f13 = f7;
						f15 = f10;
						f17 = (float)i;
						f20 = (float)(i + 1);
						f19 = (float)k;
						f21 = (float)k;
					} else if(k1 == 1) {
						f13 = f9;
						f15 = f8;
						f17 = (float)(i + 1);
						f20 = (float)i;
						f19 = (float)(k + 1);
						f21 = (float)(k + 1);
					} else if(k1 == 2) {
						f13 = f8;
						f15 = f7;
						f17 = (float)i;
						f20 = (float)i;
						f19 = (float)(k + 1);
						f21 = (float)k;
					} else {
						f13 = f10;
						f15 = f9;
						f17 = (float)(i + 1);
						f20 = (float)(i + 1);
						f19 = (float)k;
						f21 = (float)(k + 1);
					}

					flag2 = true;
					double d4 = (double)((float)(j3 + 0) / 256.0F);
					double d5 = ((double)(j3 + 16) - 0.01D) / 256.0D;
					double d6 = (double)(((float)i55 + (1.0F - f13) * 16.0F) / 256.0F);
					double d7 = (double)(((float)i55 + (1.0F - f15) * 16.0F) / 256.0F);
					double d8 = ((double)(i55 + 16) - 0.01D) / 256.0D;
					float f22 = block.getBlockBrightness(this.blockAccess, i53, j, l2);
					if(k1 < 2) {
						f22 *= f5;
					} else {
						f22 *= f6;
					}

					tessellator.setColorOpaque_F(f4 * f22 * f, f4 * f22 * f1, f4 * f22 * f2);
					tessellator.addVertexWithUV((double)f17, (double)((float)j + f13), (double)f19, d4, d6);
					tessellator.addVertexWithUV((double)f20, (double)((float)j + f15), (double)f21, d5, d7);
					tessellator.addVertexWithUV((double)f20, (double)(j + 0), (double)f21, d5, d8);
					tessellator.addVertexWithUV((double)f17, (double)(j + 0), (double)f19, d4, d8);
				}
			}

			block.minY = d;
			block.maxY = d1;
			return flag2;
		}
	}

	public float func_1224_a(int i, int j, int k, Material material) {
		int l = 0;
		float f = 0.0F;

		for(int i1 = 0; i1 < 4; ++i1) {
			int j1 = i - (i1 & 1);
			int l1 = k - (i1 >> 1 & 1);
			if(this.blockAccess.getBlockMaterial(j1, j + 1, l1) == material) {
				return 1.0F;
			}

			Material material1 = this.blockAccess.getBlockMaterial(j1, j, l1);
			if(material1 != material) {
				if(!material1.isSolid()) {
					++f;
					++l;
				}
			} else {
				int i2 = this.blockAccess.getBlockMetadata(j1, j, l1);
				if(i2 >= 8 || i2 == 0) {
					f += BlockFluid.getPercentAir(i2) * 10.0F;
					l += 10;
				}

				f += BlockFluid.getPercentAir(i2);
				++l;
			}
		}

		return 1.0F - f / (float)l;
	}

	public void renderBlockFallingSand(Block block, World world, int i, int j, int k) {
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		float f4 = block.getBlockBrightness(world, i, j, k);
		float f5 = block.getBlockBrightness(world, i, j - 1, k);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
		this.renderBottomFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(0));
		f5 = block.getBlockBrightness(world, i, j + 1, k);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
		this.renderTopFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(1));
		f5 = block.getBlockBrightness(world, i, j, k - 1);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
		this.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(2));
		f5 = block.getBlockBrightness(world, i, j, k + 1);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
		this.renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(3));
		f5 = block.getBlockBrightness(world, i - 1, j, k);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
		this.renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(4));
		f5 = block.getBlockBrightness(world, i + 1, j, k);
		if(f5 < f4) {
			f5 = f4;
		}

		tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
		this.renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getBlockTextureFromSide(5));
		tessellator.draw();
	}

	public boolean renderStandardBlock(Block block, int i, int j, int k) {
		int l = block.colorMultiplier(this.blockAccess, i, j, k);
		float f = (float)(l >> 16 & 255) / 255.0F;
		float f1 = (float)(l >> 8 & 255) / 255.0F;
		float f2 = (float)(l & 255) / 255.0F;
		if(EntityRenderer.field_28135_a) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		return Minecraft.isAmbientOcclusionEnabled() ? this.renderStandardBlockWithAmbientOcclusion(block, i, j, k, f, f1, f2) : this.renderStandardBlockWithColorMultiplier(block, i, j, k, f, f1, f2);
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block block, int i, int j, int k, float f, float f1, float f2) {
		this.enableAO = true;
		boolean flag = false;
		float f3 = this.lightValueOwn;
		float f4 = this.lightValueOwn;
		float f5 = this.lightValueOwn;
		float f6 = this.lightValueOwn;
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		boolean flag4 = true;
		boolean flag5 = true;
		boolean flag6 = true;
		this.lightValueOwn = block.getBlockBrightness(this.blockAccess, i, j, k);
		this.aoLightValueXNeg = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
		this.aoLightValueYNeg = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
		this.aoLightValueZNeg = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
		this.aoLightValueXPos = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
		this.aoLightValueYPos = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
		this.aoLightValueZPos = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
		this.field_22338_U = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j + 1, k)];
		this.field_22359_ac = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j - 1, k)];
		this.field_22334_Y = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j, k + 1)];
		this.field_22363_aa = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j, k - 1)];
		this.field_22337_V = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j + 1, k)];
		this.field_22357_ad = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j - 1, k)];
		this.field_22335_X = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j, k - 1)];
		this.field_22333_Z = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j, k + 1)];
		this.field_22336_W = Block.canBlockGrass[this.blockAccess.getBlockId(i, j + 1, k + 1)];
		this.field_22339_T = Block.canBlockGrass[this.blockAccess.getBlockId(i, j + 1, k - 1)];
		this.field_22355_ae = Block.canBlockGrass[this.blockAccess.getBlockId(i, j - 1, k + 1)];
		this.field_22361_ab = Block.canBlockGrass[this.blockAccess.getBlockId(i, j - 1, k - 1)];
		if(block.blockIndexInTexture == 3) {
			flag6 = false;
			flag5 = false;
			flag4 = false;
			flag3 = false;
			flag1 = false;
		}

		if(this.overrideBlockTexture >= 0) {
			flag6 = false;
			flag5 = false;
			flag4 = false;
			flag3 = false;
			flag1 = false;
		}

		float f12;
		float f18;
		float f24;
		float f30;
		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j - 1, k, 0)) {
			if(this.field_22352_G <= 0) {
				f30 = this.aoLightValueYNeg;
				f24 = this.aoLightValueYNeg;
				f18 = this.aoLightValueYNeg;
				f12 = this.aoLightValueYNeg;
			} else {
				--j;
				this.field_22376_n = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
				this.field_22374_p = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
				this.field_22373_q = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
				this.field_22371_s = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
				if(!this.field_22361_ab && !this.field_22357_ad) {
					this.field_22377_m = this.field_22376_n;
				} else {
					this.field_22377_m = block.getBlockBrightness(this.blockAccess, i - 1, j, k - 1);
				}

				if(!this.field_22355_ae && !this.field_22357_ad) {
					this.field_22375_o = this.field_22376_n;
				} else {
					this.field_22375_o = block.getBlockBrightness(this.blockAccess, i - 1, j, k + 1);
				}

				if(!this.field_22361_ab && !this.field_22359_ac) {
					this.field_22372_r = this.field_22371_s;
				} else {
					this.field_22372_r = block.getBlockBrightness(this.blockAccess, i + 1, j, k - 1);
				}

				if(!this.field_22355_ae && !this.field_22359_ac) {
					this.field_22370_t = this.field_22371_s;
				} else {
					this.field_22370_t = block.getBlockBrightness(this.blockAccess, i + 1, j, k + 1);
				}

				++j;
				f12 = (this.field_22375_o + this.field_22376_n + this.field_22373_q + this.aoLightValueYNeg) / 4.0F;
				f30 = (this.field_22373_q + this.aoLightValueYNeg + this.field_22370_t + this.field_22371_s) / 4.0F;
				f24 = (this.aoLightValueYNeg + this.field_22374_p + this.field_22371_s + this.field_22372_r) / 4.0F;
				f18 = (this.field_22376_n + this.field_22377_m + this.aoLightValueYNeg + this.field_22374_p) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag1 ? f : 1.0F) * 0.5F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag1 ? f1 : 1.0F) * 0.5F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag1 ? f2 : 1.0F) * 0.5F;
			this.colorRedTopLeft *= f12;
			this.colorGreenTopLeft *= f12;
			this.colorBlueTopLeft *= f12;
			this.colorRedBottomLeft *= f18;
			this.colorGreenBottomLeft *= f18;
			this.colorBlueBottomLeft *= f18;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f30;
			this.colorGreenTopRight *= f30;
			this.colorBlueTopRight *= f30;
			this.renderBottomFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 0));
			flag = true;
		}

		int l1;
		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j + 1, k, 1)) {
			if(this.field_22352_G <= 0) {
				f30 = this.aoLightValueYPos;
				f24 = this.aoLightValueYPos;
				f18 = this.aoLightValueYPos;
				f12 = this.aoLightValueYPos;
			} else {
				++j;
				this.field_22368_v = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
				this.field_22364_z = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
				this.field_22366_x = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
				this.field_22362_A = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
				if(!this.field_22339_T && !this.field_22337_V) {
					this.field_22369_u = this.field_22368_v;
				} else {
					this.field_22369_u = block.getBlockBrightness(this.blockAccess, i - 1, j, k - 1);
				}

				if(!this.field_22339_T && !this.field_22338_U) {
					this.field_22365_y = this.field_22364_z;
				} else {
					this.field_22365_y = block.getBlockBrightness(this.blockAccess, i + 1, j, k - 1);
				}

				if(!this.field_22336_W && !this.field_22337_V) {
					this.field_22367_w = this.field_22368_v;
				} else {
					this.field_22367_w = block.getBlockBrightness(this.blockAccess, i - 1, j, k + 1);
				}

				if(!this.field_22336_W && !this.field_22338_U) {
					this.field_22360_B = this.field_22364_z;
				} else {
					this.field_22360_B = block.getBlockBrightness(this.blockAccess, i + 1, j, k + 1);
				}

				--j;
				f30 = (this.field_22367_w + this.field_22368_v + this.field_22362_A + this.aoLightValueYPos) / 4.0F;
				f12 = (this.field_22362_A + this.aoLightValueYPos + this.field_22360_B + this.field_22364_z) / 4.0F;
				f18 = (this.aoLightValueYPos + this.field_22366_x + this.field_22364_z + this.field_22365_y) / 4.0F;
				f24 = (this.field_22368_v + this.field_22369_u + this.aoLightValueYPos + this.field_22366_x) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = flag2 ? f : 1.0F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = flag2 ? f1 : 1.0F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = flag2 ? f2 : 1.0F;
			this.colorRedTopLeft *= f12;
			this.colorGreenTopLeft *= f12;
			this.colorBlueTopLeft *= f12;
			this.colorRedBottomLeft *= f18;
			this.colorGreenBottomLeft *= f18;
			this.colorBlueBottomLeft *= f18;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f30;
			this.colorGreenTopRight *= f30;
			this.colorBlueTopRight *= f30;
			l1 = block.getBlockTexture(this.blockAccess, i, j, k, 1);
			this.renderTopFace(block, (double)i, (double)j, (double)k, l1);
			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j, k - 1, 2)) {
			if(this.field_22352_G <= 0) {
				f30 = this.aoLightValueZNeg;
				f24 = this.aoLightValueZNeg;
				f18 = this.aoLightValueZNeg;
				f12 = this.aoLightValueZNeg;
			} else {
				--k;
				this.field_22358_C = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
				this.field_22374_p = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
				this.field_22366_x = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
				this.field_22356_D = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
				if(!this.field_22335_X && !this.field_22361_ab) {
					this.field_22377_m = this.field_22358_C;
				} else {
					this.field_22377_m = block.getBlockBrightness(this.blockAccess, i - 1, j - 1, k);
				}

				if(!this.field_22335_X && !this.field_22339_T) {
					this.field_22369_u = this.field_22358_C;
				} else {
					this.field_22369_u = block.getBlockBrightness(this.blockAccess, i - 1, j + 1, k);
				}

				if(!this.field_22363_aa && !this.field_22361_ab) {
					this.field_22372_r = this.field_22356_D;
				} else {
					this.field_22372_r = block.getBlockBrightness(this.blockAccess, i + 1, j - 1, k);
				}

				if(!this.field_22363_aa && !this.field_22339_T) {
					this.field_22365_y = this.field_22356_D;
				} else {
					this.field_22365_y = block.getBlockBrightness(this.blockAccess, i + 1, j + 1, k);
				}

				++k;
				f12 = (this.field_22358_C + this.field_22369_u + this.aoLightValueZNeg + this.field_22366_x) / 4.0F;
				f18 = (this.aoLightValueZNeg + this.field_22366_x + this.field_22356_D + this.field_22365_y) / 4.0F;
				f24 = (this.field_22374_p + this.aoLightValueZNeg + this.field_22372_r + this.field_22356_D) / 4.0F;
				f30 = (this.field_22377_m + this.field_22358_C + this.field_22374_p + this.aoLightValueZNeg) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag3 ? f : 1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag3 ? f1 : 1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag3 ? f2 : 1.0F) * 0.8F;
			this.colorRedTopLeft *= f12;
			this.colorGreenTopLeft *= f12;
			this.colorBlueTopLeft *= f12;
			this.colorRedBottomLeft *= f18;
			this.colorGreenBottomLeft *= f18;
			this.colorBlueBottomLeft *= f18;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f30;
			this.colorGreenTopRight *= f30;
			this.colorBlueTopRight *= f30;
			l1 = block.getBlockTexture(this.blockAccess, i, j, k, 2);
			this.renderEastFace(block, (double)i, (double)j, (double)k, l1);
			if(Tessellator.instance.defaultTexture && cfgGrassFix && l1 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f;
				this.colorRedBottomLeft *= f;
				this.colorRedBottomRight *= f;
				this.colorRedTopRight *= f;
				this.colorGreenTopLeft *= f1;
				this.colorGreenBottomLeft *= f1;
				this.colorGreenBottomRight *= f1;
				this.colorGreenTopRight *= f1;
				this.colorBlueTopLeft *= f2;
				this.colorBlueBottomLeft *= f2;
				this.colorBlueBottomRight *= f2;
				this.colorBlueTopRight *= f2;
				this.renderEastFace(block, (double)i, (double)j, (double)k, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j, k + 1, 3)) {
			if(this.field_22352_G <= 0) {
				f30 = this.aoLightValueZPos;
				f24 = this.aoLightValueZPos;
				f18 = this.aoLightValueZPos;
				f12 = this.aoLightValueZPos;
			} else {
				++k;
				this.field_22354_E = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
				this.field_22353_F = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
				this.field_22373_q = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
				this.field_22362_A = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
				if(!this.field_22333_Z && !this.field_22355_ae) {
					this.field_22375_o = this.field_22354_E;
				} else {
					this.field_22375_o = block.getBlockBrightness(this.blockAccess, i - 1, j - 1, k);
				}

				if(!this.field_22333_Z && !this.field_22336_W) {
					this.field_22367_w = this.field_22354_E;
				} else {
					this.field_22367_w = block.getBlockBrightness(this.blockAccess, i - 1, j + 1, k);
				}

				if(!this.field_22334_Y && !this.field_22355_ae) {
					this.field_22370_t = this.field_22353_F;
				} else {
					this.field_22370_t = block.getBlockBrightness(this.blockAccess, i + 1, j - 1, k);
				}

				if(!this.field_22334_Y && !this.field_22336_W) {
					this.field_22360_B = this.field_22353_F;
				} else {
					this.field_22360_B = block.getBlockBrightness(this.blockAccess, i + 1, j + 1, k);
				}

				--k;
				f12 = (this.field_22354_E + this.field_22367_w + this.aoLightValueZPos + this.field_22362_A) / 4.0F;
				f30 = (this.aoLightValueZPos + this.field_22362_A + this.field_22353_F + this.field_22360_B) / 4.0F;
				f24 = (this.field_22373_q + this.aoLightValueZPos + this.field_22370_t + this.field_22353_F) / 4.0F;
				f18 = (this.field_22375_o + this.field_22354_E + this.field_22373_q + this.aoLightValueZPos) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag4 ? f : 1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag4 ? f1 : 1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag4 ? f2 : 1.0F) * 0.8F;
			this.colorRedTopLeft *= f12;
			this.colorGreenTopLeft *= f12;
			this.colorBlueTopLeft *= f12;
			this.colorRedBottomLeft *= f18;
			this.colorGreenBottomLeft *= f18;
			this.colorBlueBottomLeft *= f18;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f30;
			this.colorGreenTopRight *= f30;
			this.colorBlueTopRight *= f30;
			l1 = block.getBlockTexture(this.blockAccess, i, j, k, 3);
			this.renderWestFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 3));
			if(Tessellator.instance.defaultTexture && cfgGrassFix && l1 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f;
				this.colorRedBottomLeft *= f;
				this.colorRedBottomRight *= f;
				this.colorRedTopRight *= f;
				this.colorGreenTopLeft *= f1;
				this.colorGreenBottomLeft *= f1;
				this.colorGreenBottomRight *= f1;
				this.colorGreenTopRight *= f1;
				this.colorBlueTopLeft *= f2;
				this.colorBlueBottomLeft *= f2;
				this.colorBlueBottomRight *= f2;
				this.colorBlueTopRight *= f2;
				this.renderWestFace(block, (double)i, (double)j, (double)k, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i - 1, j, k, 4)) {
			if(this.field_22352_G <= 0) {
				f30 = this.aoLightValueXNeg;
				f24 = this.aoLightValueXNeg;
				f18 = this.aoLightValueXNeg;
				f12 = this.aoLightValueXNeg;
			} else {
				--i;
				this.field_22376_n = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
				this.field_22358_C = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
				this.field_22354_E = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
				this.field_22368_v = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
				if(!this.field_22335_X && !this.field_22357_ad) {
					this.field_22377_m = this.field_22358_C;
				} else {
					this.field_22377_m = block.getBlockBrightness(this.blockAccess, i, j - 1, k - 1);
				}

				if(!this.field_22333_Z && !this.field_22357_ad) {
					this.field_22375_o = this.field_22354_E;
				} else {
					this.field_22375_o = block.getBlockBrightness(this.blockAccess, i, j - 1, k + 1);
				}

				if(!this.field_22335_X && !this.field_22337_V) {
					this.field_22369_u = this.field_22358_C;
				} else {
					this.field_22369_u = block.getBlockBrightness(this.blockAccess, i, j + 1, k - 1);
				}

				if(!this.field_22333_Z && !this.field_22337_V) {
					this.field_22367_w = this.field_22354_E;
				} else {
					this.field_22367_w = block.getBlockBrightness(this.blockAccess, i, j + 1, k + 1);
				}

				++i;
				f30 = (this.field_22376_n + this.field_22375_o + this.aoLightValueXNeg + this.field_22354_E) / 4.0F;
				f12 = (this.aoLightValueXNeg + this.field_22354_E + this.field_22368_v + this.field_22367_w) / 4.0F;
				f18 = (this.field_22358_C + this.aoLightValueXNeg + this.field_22369_u + this.field_22368_v) / 4.0F;
				f24 = (this.field_22377_m + this.field_22376_n + this.field_22358_C + this.aoLightValueXNeg) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag5 ? f : 1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag5 ? f1 : 1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag5 ? f2 : 1.0F) * 0.6F;
			this.colorRedTopLeft *= f12;
			this.colorGreenTopLeft *= f12;
			this.colorBlueTopLeft *= f12;
			this.colorRedBottomLeft *= f18;
			this.colorGreenBottomLeft *= f18;
			this.colorBlueBottomLeft *= f18;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f30;
			this.colorGreenTopRight *= f30;
			this.colorBlueTopRight *= f30;
			l1 = block.getBlockTexture(this.blockAccess, i, j, k, 4);
			this.renderNorthFace(block, (double)i, (double)j, (double)k, l1);
			if(Tessellator.instance.defaultTexture && cfgGrassFix && l1 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f;
				this.colorRedBottomLeft *= f;
				this.colorRedBottomRight *= f;
				this.colorRedTopRight *= f;
				this.colorGreenTopLeft *= f1;
				this.colorGreenBottomLeft *= f1;
				this.colorGreenBottomRight *= f1;
				this.colorGreenTopRight *= f1;
				this.colorBlueTopLeft *= f2;
				this.colorBlueBottomLeft *= f2;
				this.colorBlueBottomRight *= f2;
				this.colorBlueTopRight *= f2;
				this.renderNorthFace(block, (double)i, (double)j, (double)k, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i + 1, j, k, 5)) {
			if(this.field_22352_G <= 0) {
				f30 = this.aoLightValueXPos;
				f24 = this.aoLightValueXPos;
				f18 = this.aoLightValueXPos;
				f12 = this.aoLightValueXPos;
			} else {
				++i;
				this.field_22371_s = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
				this.field_22356_D = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
				this.field_22353_F = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
				this.field_22364_z = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
				if(!this.field_22359_ac && !this.field_22363_aa) {
					this.field_22372_r = this.field_22356_D;
				} else {
					this.field_22372_r = block.getBlockBrightness(this.blockAccess, i, j - 1, k - 1);
				}

				if(!this.field_22359_ac && !this.field_22334_Y) {
					this.field_22370_t = this.field_22353_F;
				} else {
					this.field_22370_t = block.getBlockBrightness(this.blockAccess, i, j - 1, k + 1);
				}

				if(!this.field_22338_U && !this.field_22363_aa) {
					this.field_22365_y = this.field_22356_D;
				} else {
					this.field_22365_y = block.getBlockBrightness(this.blockAccess, i, j + 1, k - 1);
				}

				if(!this.field_22338_U && !this.field_22334_Y) {
					this.field_22360_B = this.field_22353_F;
				} else {
					this.field_22360_B = block.getBlockBrightness(this.blockAccess, i, j + 1, k + 1);
				}

				--i;
				f12 = (this.field_22371_s + this.field_22370_t + this.aoLightValueXPos + this.field_22353_F) / 4.0F;
				f30 = (this.aoLightValueXPos + this.field_22353_F + this.field_22364_z + this.field_22360_B) / 4.0F;
				f24 = (this.field_22356_D + this.aoLightValueXPos + this.field_22365_y + this.field_22364_z) / 4.0F;
				f18 = (this.field_22372_r + this.field_22371_s + this.field_22356_D + this.aoLightValueXPos) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag6 ? f : 1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag6 ? f1 : 1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag6 ? f2 : 1.0F) * 0.6F;
			this.colorRedTopLeft *= f12;
			this.colorGreenTopLeft *= f12;
			this.colorBlueTopLeft *= f12;
			this.colorRedBottomLeft *= f18;
			this.colorGreenBottomLeft *= f18;
			this.colorBlueBottomLeft *= f18;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f30;
			this.colorGreenTopRight *= f30;
			this.colorBlueTopRight *= f30;
			l1 = block.getBlockTexture(this.blockAccess, i, j, k, 5);
			this.renderSouthFace(block, (double)i, (double)j, (double)k, l1);
			if(Tessellator.instance.defaultTexture && cfgGrassFix && l1 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f;
				this.colorRedBottomLeft *= f;
				this.colorRedBottomRight *= f;
				this.colorRedTopRight *= f;
				this.colorGreenTopLeft *= f1;
				this.colorGreenBottomLeft *= f1;
				this.colorGreenBottomRight *= f1;
				this.colorGreenTopRight *= f1;
				this.colorBlueTopLeft *= f2;
				this.colorBlueBottomLeft *= f2;
				this.colorBlueBottomRight *= f2;
				this.colorBlueTopRight *= f2;
				this.renderSouthFace(block, (double)i, (double)j, (double)k, 38);
			}

			flag = true;
		}

		this.enableAO = false;
		return flag;
	}

	public boolean renderStandardBlockWithColorMultiplier(Block block, int i, int j, int k, float f, float f1, float f2) {
		this.enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		float f7 = f4 * f;
		float f8 = f4 * f1;
		float f9 = f4 * f2;
		float f10 = f3;
		float f11 = f5;
		float f12 = f6;
		float f13 = f3;
		float f14 = f5;
		float f15 = f6;
		float f16 = f3;
		float f17 = f5;
		float f18 = f6;
		if(block != Block.grass) {
			f10 = f3 * f;
			f11 = f5 * f;
			f12 = f6 * f;
			f13 = f3 * f1;
			f14 = f5 * f1;
			f15 = f6 * f1;
			f16 = f3 * f2;
			f17 = f5 * f2;
			f18 = f6 * f2;
		}

		float f19 = block.getBlockBrightness(this.blockAccess, i, j, k);
		float f25;
		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j - 1, k, 0)) {
			f25 = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
			tessellator.setColorOpaque_F(f10 * f25, f13 * f25, f16 * f25);
			this.renderBottomFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 0));
			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j + 1, k, 1)) {
			f25 = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
			if(block.maxY != 1.0D && !block.blockMaterial.getIsLiquid()) {
				f25 = f19;
			}

			tessellator.setColorOpaque_F(f7 * f25, f8 * f25, f9 * f25);
			this.renderTopFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 1));
			flag = true;
		}

		int k1;
		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j, k - 1, 2)) {
			f25 = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
			if(block.minZ > 0.0D) {
				f25 = f19;
			}

			tessellator.setColorOpaque_F(f11 * f25, f14 * f25, f17 * f25);
			k1 = block.getBlockTexture(this.blockAccess, i, j, k, 2);
			this.renderEastFace(block, (double)i, (double)j, (double)k, k1);
			if(Tessellator.instance.defaultTexture && cfgGrassFix && k1 == 3 && this.overrideBlockTexture < 0) {
				tessellator.setColorOpaque_F(f11 * f25 * f, f14 * f25 * f1, f17 * f25 * f2);
				this.renderEastFace(block, (double)i, (double)j, (double)k, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j, k + 1, 3)) {
			f25 = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
			if(block.maxZ < 1.0D) {
				f25 = f19;
			}

			tessellator.setColorOpaque_F(f11 * f25, f14 * f25, f17 * f25);
			k1 = block.getBlockTexture(this.blockAccess, i, j, k, 3);
			this.renderWestFace(block, (double)i, (double)j, (double)k, k1);
			if(Tessellator.instance.defaultTexture && cfgGrassFix && k1 == 3 && this.overrideBlockTexture < 0) {
				tessellator.setColorOpaque_F(f11 * f25 * f, f14 * f25 * f1, f17 * f25 * f2);
				this.renderWestFace(block, (double)i, (double)j, (double)k, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i - 1, j, k, 4)) {
			f25 = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
			if(block.minX > 0.0D) {
				f25 = f19;
			}

			tessellator.setColorOpaque_F(f12 * f25, f15 * f25, f18 * f25);
			k1 = block.getBlockTexture(this.blockAccess, i, j, k, 4);
			this.renderNorthFace(block, (double)i, (double)j, (double)k, k1);
			if(Tessellator.instance.defaultTexture && cfgGrassFix && k1 == 3 && this.overrideBlockTexture < 0) {
				tessellator.setColorOpaque_F(f12 * f25 * f, f15 * f25 * f1, f18 * f25 * f2);
				this.renderNorthFace(block, (double)i, (double)j, (double)k, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i + 1, j, k, 5)) {
			f25 = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
			if(block.maxX < 1.0D) {
				f25 = f19;
			}

			tessellator.setColorOpaque_F(f12 * f25, f15 * f25, f18 * f25);
			k1 = block.getBlockTexture(this.blockAccess, i, j, k, 5);
			this.renderSouthFace(block, (double)i, (double)j, (double)k, k1);
			if(Tessellator.instance.defaultTexture && cfgGrassFix && k1 == 3 && this.overrideBlockTexture < 0) {
				tessellator.setColorOpaque_F(f12 * f25 * f, f15 * f25 * f1, f18 * f25 * f2);
				this.renderSouthFace(block, (double)i, (double)j, (double)k, 38);
			}

			flag = true;
		}

		return flag;
	}

	public boolean renderBlockCactus(Block block, int i, int j, int k) {
		int l = block.colorMultiplier(this.blockAccess, i, j, k);
		float f = (float)(l >> 16 & 255) / 255.0F;
		float f1 = (float)(l >> 8 & 255) / 255.0F;
		float f2 = (float)(l & 255) / 255.0F;
		if(EntityRenderer.field_28135_a) {
			float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
			float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
			float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
			f = f3;
			f1 = f4;
			f2 = f5;
		}

		return this.func_1230_b(block, i, j, k, f, f1, f2);
	}

	public boolean func_1230_b(Block block, int i, int j, int k, float f, float f1, float f2) {
		Tessellator tessellator = Tessellator.instance;
		boolean flag = false;
		float f3 = 0.5F;
		float f4 = 1.0F;
		float f5 = 0.8F;
		float f6 = 0.6F;
		float f7 = f3 * f;
		float f8 = f4 * f;
		float f9 = f5 * f;
		float f10 = f6 * f;
		float f11 = f3 * f1;
		float f12 = f4 * f1;
		float f13 = f5 * f1;
		float f14 = f6 * f1;
		float f15 = f3 * f2;
		float f16 = f4 * f2;
		float f17 = f5 * f2;
		float f18 = f6 * f2;
		float f19 = 0.0625F;
		float f20 = block.getBlockBrightness(this.blockAccess, i, j, k);
		float f26;
		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j - 1, k, 0)) {
			f26 = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
			tessellator.setColorOpaque_F(f7 * f26, f11 * f26, f15 * f26);
			this.renderBottomFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 0));
			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j + 1, k, 1)) {
			f26 = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
			if(block.maxY != 1.0D && !block.blockMaterial.getIsLiquid()) {
				f26 = f20;
			}

			tessellator.setColorOpaque_F(f8 * f26, f12 * f26, f16 * f26);
			this.renderTopFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 1));
			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j, k - 1, 2)) {
			f26 = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
			if(block.minZ > 0.0D) {
				f26 = f20;
			}

			tessellator.setColorOpaque_F(f9 * f26, f13 * f26, f17 * f26);
			tessellator.setTranslationF(0.0F, 0.0F, f19);
			this.renderEastFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 2));
			tessellator.setTranslationF(0.0F, 0.0F, -f19);
			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i, j, k + 1, 3)) {
			f26 = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
			if(block.maxZ < 1.0D) {
				f26 = f20;
			}

			tessellator.setColorOpaque_F(f9 * f26, f13 * f26, f17 * f26);
			tessellator.setTranslationF(0.0F, 0.0F, -f19);
			this.renderWestFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 3));
			tessellator.setTranslationF(0.0F, 0.0F, f19);
			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i - 1, j, k, 4)) {
			f26 = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
			if(block.minX > 0.0D) {
				f26 = f20;
			}

			tessellator.setColorOpaque_F(f10 * f26, f14 * f26, f18 * f26);
			tessellator.setTranslationF(f19, 0.0F, 0.0F);
			this.renderNorthFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 4));
			tessellator.setTranslationF(-f19, 0.0F, 0.0F);
			flag = true;
		}

		if(this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, i + 1, j, k, 5)) {
			f26 = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
			if(block.maxX < 1.0D) {
				f26 = f20;
			}

			tessellator.setColorOpaque_F(f10 * f26, f14 * f26, f18 * f26);
			tessellator.setTranslationF(-f19, 0.0F, 0.0F);
			this.renderSouthFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 5));
			tessellator.setTranslationF(f19, 0.0F, 0.0F);
			flag = true;
		}

		return flag;
	}

	public boolean renderBlockFence(Block block, int i, int j, int k) {
		boolean flag = false;
		float f = 0.375F;
		float f1 = 0.625F;
		block.setBlockBounds(f, 0.0F, f, f1, 1.0F, f1);
		this.renderStandardBlock(block, i, j, k);
		flag = true;
		boolean flag1 = false;
		boolean flag2 = false;
		if(this.blockAccess.getBlockId(i - 1, j, k) == block.blockID || this.blockAccess.getBlockId(i + 1, j, k) == block.blockID) {
			flag1 = true;
		}

		if(this.blockAccess.getBlockId(i, j, k - 1) == block.blockID || this.blockAccess.getBlockId(i, j, k + 1) == block.blockID) {
			flag2 = true;
		}

		boolean flag3 = this.blockAccess.getBlockId(i - 1, j, k) == block.blockID;
		boolean flag4 = this.blockAccess.getBlockId(i + 1, j, k) == block.blockID;
		boolean flag5 = this.blockAccess.getBlockId(i, j, k - 1) == block.blockID;
		boolean flag6 = this.blockAccess.getBlockId(i, j, k + 1) == block.blockID;
		if(!flag1 && !flag2) {
			flag1 = true;
		}

		f = 0.4375F;
		f1 = 0.5625F;
		float f2 = 0.75F;
		float f3 = 0.9375F;
		float f4 = flag3 ? 0.0F : f;
		float f5 = flag4 ? 1.0F : f1;
		float f6 = flag5 ? 0.0F : f;
		float f7 = flag6 ? 1.0F : f1;
		if(flag1) {
			block.setBlockBounds(f4, f2, f, f5, f3, f1);
			this.renderStandardBlock(block, i, j, k);
			flag = true;
		}

		if(flag2) {
			block.setBlockBounds(f, f2, f6, f1, f3, f7);
			this.renderStandardBlock(block, i, j, k);
			flag = true;
		}

		f2 = 0.375F;
		f3 = 0.5625F;
		if(flag1) {
			block.setBlockBounds(f4, f2, f, f5, f3, f1);
			this.renderStandardBlock(block, i, j, k);
			flag = true;
		}

		if(flag2) {
			block.setBlockBounds(f, f2, f6, f1, f3, f7);
			this.renderStandardBlock(block, i, j, k);
			flag = true;
		}

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return flag;
	}

	public boolean renderBlockStairs(Block block, int i, int j, int k) {
		boolean flag = false;
		int l = this.blockAccess.getBlockMetadata(i, j, k);
		if(l == 0) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			block.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			flag = true;
		} else if(l == 1) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			block.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			flag = true;
		} else if(l == 2) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			this.renderStandardBlock(block, i, j, k);
			block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			flag = true;
		} else if(l == 3) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			this.renderStandardBlock(block, i, j, k);
			block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			this.renderStandardBlock(block, i, j, k);
			flag = true;
		}

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return flag;
	}

	public boolean renderBlockDoor(Block block, int i, int j, int k) {
		Tessellator tessellator = Tessellator.instance;
		BlockDoor blockdoor = (BlockDoor)block;
		boolean flag = false;
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		float f4 = block.getBlockBrightness(this.blockAccess, i, j, k);
		float f5 = block.getBlockBrightness(this.blockAccess, i, j - 1, k);
		if(blockdoor.minY > 0.0D) {
			f5 = f4;
		}

		if(Block.lightValue[block.blockID] > 0) {
			f5 = 1.0F;
		}

		tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
		this.renderBottomFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 0));
		flag = true;
		f5 = block.getBlockBrightness(this.blockAccess, i, j + 1, k);
		if(blockdoor.maxY < 1.0D) {
			f5 = f4;
		}

		if(Block.lightValue[block.blockID] > 0) {
			f5 = 1.0F;
		}

		tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
		this.renderTopFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(this.blockAccess, i, j, k, 1));
		flag = true;
		f5 = block.getBlockBrightness(this.blockAccess, i, j, k - 1);
		if(blockdoor.minZ > 0.0D) {
			f5 = f4;
		}

		if(Block.lightValue[block.blockID] > 0) {
			f5 = 1.0F;
		}

		tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
		int l = block.getBlockTexture(this.blockAccess, i, j, k, 2);
		if(l < 0) {
			this.flipTexture = true;
			l = -l;
		}

		this.renderEastFace(block, (double)i, (double)j, (double)k, l);
		flag = true;
		this.flipTexture = false;
		f5 = block.getBlockBrightness(this.blockAccess, i, j, k + 1);
		if(blockdoor.maxZ < 1.0D) {
			f5 = f4;
		}

		if(Block.lightValue[block.blockID] > 0) {
			f5 = 1.0F;
		}

		tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
		l = block.getBlockTexture(this.blockAccess, i, j, k, 3);
		if(l < 0) {
			this.flipTexture = true;
			l = -l;
		}

		this.renderWestFace(block, (double)i, (double)j, (double)k, l);
		flag = true;
		this.flipTexture = false;
		f5 = block.getBlockBrightness(this.blockAccess, i - 1, j, k);
		if(blockdoor.minX > 0.0D) {
			f5 = f4;
		}

		if(Block.lightValue[block.blockID] > 0) {
			f5 = 1.0F;
		}

		tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
		l = block.getBlockTexture(this.blockAccess, i, j, k, 4);
		if(l < 0) {
			this.flipTexture = true;
			l = -l;
		}

		this.renderNorthFace(block, (double)i, (double)j, (double)k, l);
		flag = true;
		this.flipTexture = false;
		f5 = block.getBlockBrightness(this.blockAccess, i + 1, j, k);
		if(blockdoor.maxX < 1.0D) {
			f5 = f4;
		}

		if(Block.lightValue[block.blockID] > 0) {
			f5 = 1.0F;
		}

		tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
		l = block.getBlockTexture(this.blockAccess, i, j, k, 5);
		if(l < 0) {
			this.flipTexture = true;
			l = -l;
		}

		this.renderSouthFace(block, (double)i, (double)j, (double)k, l);
		flag = true;
		this.flipTexture = false;
		return flag;
	}

	public void renderBottomFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d3 = ((double)j + block.minX * 16.0D) / 256.0D;
		double d4 = ((double)j + block.maxX * 16.0D - 0.01D) / 256.0D;
		double d5 = ((double)k + block.minZ * 16.0D) / 256.0D;
		double d6 = ((double)k + block.maxZ * 16.0D - 0.01D) / 256.0D;
		if(block.minX < 0.0D || block.maxX > 1.0D) {
			d3 = (double)(((float)j + 0.0F) / 256.0F);
			d4 = (double)(((float)j + 15.99F) / 256.0F);
		}

		if(block.minZ < 0.0D || block.maxZ > 1.0D) {
			d5 = (double)(((float)k + 0.0F) / 256.0F);
			d6 = (double)(((float)k + 15.99F) / 256.0F);
		}

		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		if(this.field_31082_l == 2) {
			d3 = ((double)j + block.minZ * 16.0D) / 256.0D;
			d5 = ((double)(k + 16) - block.maxX * 16.0D) / 256.0D;
			d4 = ((double)j + block.maxZ * 16.0D) / 256.0D;
			d6 = ((double)(k + 16) - block.minX * 16.0D) / 256.0D;
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(this.field_31082_l == 1) {
			d3 = ((double)(j + 16) - block.maxZ * 16.0D) / 256.0D;
			d5 = ((double)k + block.minX * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.minZ * 16.0D) / 256.0D;
			d6 = ((double)k + block.maxX * 16.0D) / 256.0D;
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(this.field_31082_l == 3) {
			d3 = ((double)(j + 16) - block.minX * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.maxX * 16.0D - 0.01D) / 256.0D;
			d5 = ((double)(k + 16) - block.minZ * 16.0D) / 256.0D;
			d6 = ((double)(k + 16) - block.maxZ * 16.0D - 0.01D) / 256.0D;
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = d + block.minX;
		double d12 = d + block.maxX;
		double d13 = d1 + block.minY;
		double d14 = d2 + block.minZ;
		double d15 = d2 + block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
		} else {
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
		}

	}

	public void renderTopFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d3 = ((double)j + block.minX * 16.0D) / 256.0D;
		double d4 = ((double)j + block.maxX * 16.0D - 0.01D) / 256.0D;
		double d5 = ((double)k + block.minZ * 16.0D) / 256.0D;
		double d6 = ((double)k + block.maxZ * 16.0D - 0.01D) / 256.0D;
		if(block.minX < 0.0D || block.maxX > 1.0D) {
			d3 = (double)(((float)j + 0.0F) / 256.0F);
			d4 = (double)(((float)j + 15.99F) / 256.0F);
		}

		if(block.minZ < 0.0D || block.maxZ > 1.0D) {
			d5 = (double)(((float)k + 0.0F) / 256.0F);
			d6 = (double)(((float)k + 15.99F) / 256.0F);
		}

		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		if(this.field_31083_k == 1) {
			d3 = ((double)j + block.minZ * 16.0D) / 256.0D;
			d5 = ((double)(k + 16) - block.maxX * 16.0D) / 256.0D;
			d4 = ((double)j + block.maxZ * 16.0D) / 256.0D;
			d6 = ((double)(k + 16) - block.minX * 16.0D) / 256.0D;
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(this.field_31083_k == 2) {
			d3 = ((double)(j + 16) - block.maxZ * 16.0D) / 256.0D;
			d5 = ((double)k + block.minX * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.minZ * 16.0D) / 256.0D;
			d6 = ((double)k + block.maxX * 16.0D) / 256.0D;
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(this.field_31083_k == 3) {
			d3 = ((double)(j + 16) - block.minX * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.maxX * 16.0D - 0.01D) / 256.0D;
			d5 = ((double)(k + 16) - block.minZ * 16.0D) / 256.0D;
			d6 = ((double)(k + 16) - block.maxZ * 16.0D - 0.01D) / 256.0D;
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}

		double d11 = d + block.minX;
		double d12 = d + block.maxX;
		double d13 = d1 + block.maxY;
		double d14 = d2 + block.minZ;
		double d15 = d2 + block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
		} else {
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
			tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
			tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
		}

	}

	public void renderEastFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d3 = ((double)j + block.minX * 16.0D) / 256.0D;
		double d4 = ((double)j + block.maxX * 16.0D - 0.01D) / 256.0D;
		double d5 = ((double)(k + 16) - block.maxY * 16.0D) / 256.0D;
		double d6 = ((double)(k + 16) - block.minY * 16.0D - 0.01D) / 256.0D;
		double d8;
		if(this.flipTexture) {
			d8 = d3;
			d3 = d4;
			d4 = d8;
		}

		if(block.minX < 0.0D || block.maxX > 1.0D) {
			d3 = (double)(((float)j + 0.0F) / 256.0F);
			d4 = (double)(((float)j + 15.99F) / 256.0F);
		}

		if(block.minY < 0.0D || block.maxY > 1.0D) {
			d5 = (double)(((float)k + 0.0F) / 256.0F);
			d6 = (double)(((float)k + 15.99F) / 256.0F);
		}

		d8 = d4;
		double d9 = d3;
		double d10 = d5;
		double d11 = d6;
		if(this.field_31087_g == 2) {
			d3 = ((double)j + block.minY * 16.0D) / 256.0D;
			d5 = ((double)(k + 16) - block.minX * 16.0D) / 256.0D;
			d4 = ((double)j + block.maxY * 16.0D) / 256.0D;
			d6 = ((double)(k + 16) - block.maxX * 16.0D) / 256.0D;
			d10 = d5;
			d11 = d6;
			d8 = d3;
			d9 = d4;
			d5 = d6;
			d6 = d10;
		} else if(this.field_31087_g == 1) {
			d3 = ((double)(j + 16) - block.maxY * 16.0D) / 256.0D;
			d5 = ((double)k + block.maxX * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.minY * 16.0D) / 256.0D;
			d6 = ((double)k + block.minX * 16.0D) / 256.0D;
			d8 = d4;
			d9 = d3;
			d3 = d4;
			d4 = d9;
			d10 = d6;
			d11 = d5;
		} else if(this.field_31087_g == 3) {
			d3 = ((double)(j + 16) - block.minX * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.maxX * 16.0D - 0.01D) / 256.0D;
			d5 = ((double)k + block.maxY * 16.0D) / 256.0D;
			d6 = ((double)k + block.minY * 16.0D - 0.01D) / 256.0D;
			d8 = d4;
			d9 = d3;
			d10 = d5;
			d11 = d6;
		}

		double d12 = d + block.minX;
		double d13 = d + block.maxX;
		double d14 = d1 + block.minY;
		double d15 = d1 + block.maxY;
		double d16 = d2 + block.minZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.addVertexWithUV(d12, d15, d16, d8, d10);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.addVertexWithUV(d13, d15, d16, d3, d5);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.addVertexWithUV(d13, d14, d16, d9, d11);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.addVertexWithUV(d12, d14, d16, d4, d6);
		} else {
			tessellator.addVertexWithUV(d12, d15, d16, d8, d10);
			tessellator.addVertexWithUV(d13, d15, d16, d3, d5);
			tessellator.addVertexWithUV(d13, d14, d16, d9, d11);
			tessellator.addVertexWithUV(d12, d14, d16, d4, d6);
		}

	}

	public void renderWestFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d3 = ((double)j + block.minX * 16.0D) / 256.0D;
		double d4 = ((double)j + block.maxX * 16.0D - 0.01D) / 256.0D;
		double d5 = ((double)(k + 16) - block.maxY * 16.0D) / 256.0D;
		double d6 = ((double)(k + 16) - block.minY * 16.0D - 0.01D) / 256.0D;
		double d8;
		if(this.flipTexture) {
			d8 = d3;
			d3 = d4;
			d4 = d8;
		}

		if(block.minX < 0.0D || block.maxX > 1.0D) {
			d3 = (double)(((float)j + 0.0F) / 256.0F);
			d4 = (double)(((float)j + 15.99F) / 256.0F);
		}

		if(block.minY < 0.0D || block.maxY > 1.0D) {
			d5 = (double)(((float)k + 0.0F) / 256.0F);
			d6 = (double)(((float)k + 15.99F) / 256.0F);
		}

		d8 = d4;
		double d9 = d3;
		double d10 = d5;
		double d11 = d6;
		if(this.field_31086_h == 1) {
			d3 = ((double)j + block.minY * 16.0D) / 256.0D;
			d6 = ((double)(k + 16) - block.minX * 16.0D) / 256.0D;
			d4 = ((double)j + block.maxY * 16.0D) / 256.0D;
			d5 = ((double)(k + 16) - block.maxX * 16.0D) / 256.0D;
			d10 = d5;
			d11 = d6;
			d8 = d3;
			d9 = d4;
			d5 = d6;
			d6 = d10;
		} else if(this.field_31086_h == 2) {
			d3 = ((double)(j + 16) - block.maxY * 16.0D) / 256.0D;
			d5 = ((double)k + block.minX * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.minY * 16.0D) / 256.0D;
			d6 = ((double)k + block.maxX * 16.0D) / 256.0D;
			d8 = d4;
			d9 = d3;
			d3 = d4;
			d4 = d9;
			d10 = d6;
			d11 = d5;
		} else if(this.field_31086_h == 3) {
			d3 = ((double)(j + 16) - block.minX * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.maxX * 16.0D - 0.01D) / 256.0D;
			d5 = ((double)k + block.maxY * 16.0D) / 256.0D;
			d6 = ((double)k + block.minY * 16.0D - 0.01D) / 256.0D;
			d8 = d4;
			d9 = d3;
			d10 = d5;
			d11 = d6;
		}

		double d12 = d + block.minX;
		double d13 = d + block.maxX;
		double d14 = d1 + block.minY;
		double d15 = d1 + block.maxY;
		double d16 = d2 + block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.addVertexWithUV(d12, d15, d16, d3, d5);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.addVertexWithUV(d13, d14, d16, d4, d6);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.addVertexWithUV(d13, d15, d16, d8, d10);
		} else {
			tessellator.addVertexWithUV(d12, d15, d16, d3, d5);
			tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
			tessellator.addVertexWithUV(d13, d14, d16, d4, d6);
			tessellator.addVertexWithUV(d13, d15, d16, d8, d10);
		}

	}

	public void renderNorthFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d3 = ((double)j + block.minZ * 16.0D) / 256.0D;
		double d4 = ((double)j + block.maxZ * 16.0D - 0.01D) / 256.0D;
		double d5 = ((double)(k + 16) - block.maxY * 16.0D) / 256.0D;
		double d6 = ((double)(k + 16) - block.minY * 16.0D - 0.01D) / 256.0D;
		double d8;
		if(this.flipTexture) {
			d8 = d3;
			d3 = d4;
			d4 = d8;
		}

		if(block.minZ < 0.0D || block.maxZ > 1.0D) {
			d3 = (double)(((float)j + 0.0F) / 256.0F);
			d4 = (double)(((float)j + 15.99F) / 256.0F);
		}

		if(block.minY < 0.0D || block.maxY > 1.0D) {
			d5 = (double)(((float)k + 0.0F) / 256.0F);
			d6 = (double)(((float)k + 15.99F) / 256.0F);
		}

		d8 = d4;
		double d9 = d3;
		double d10 = d5;
		double d11 = d6;
		if(this.field_31084_j == 1) {
			d3 = ((double)j + block.minY * 16.0D) / 256.0D;
			d5 = ((double)(k + 16) - block.maxZ * 16.0D) / 256.0D;
			d4 = ((double)j + block.maxY * 16.0D) / 256.0D;
			d6 = ((double)(k + 16) - block.minZ * 16.0D) / 256.0D;
			d10 = d5;
			d11 = d6;
			d8 = d3;
			d9 = d4;
			d5 = d6;
			d6 = d10;
		} else if(this.field_31084_j == 2) {
			d3 = ((double)(j + 16) - block.maxY * 16.0D) / 256.0D;
			d5 = ((double)k + block.minZ * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.minY * 16.0D) / 256.0D;
			d6 = ((double)k + block.maxZ * 16.0D) / 256.0D;
			d8 = d4;
			d9 = d3;
			d3 = d4;
			d4 = d9;
			d10 = d6;
			d11 = d5;
		} else if(this.field_31084_j == 3) {
			d3 = ((double)(j + 16) - block.minZ * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.maxZ * 16.0D - 0.01D) / 256.0D;
			d5 = ((double)k + block.maxY * 16.0D) / 256.0D;
			d6 = ((double)k + block.minY * 16.0D - 0.01D) / 256.0D;
			d8 = d4;
			d9 = d3;
			d10 = d5;
			d11 = d6;
		}

		double d12 = d + block.minX;
		double d13 = d1 + block.minY;
		double d14 = d1 + block.maxY;
		double d15 = d2 + block.minZ;
		double d16 = d2 + block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
		} else {
			tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
			tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
			tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
			tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
		}

	}

	public void renderSouthFace(Block block, double d, double d1, double d2, int i) {
		Tessellator tessellator = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i = this.overrideBlockTexture;
		}

		int j = (i & 15) << 4;
		int k = i & 240;
		double d3 = ((double)j + block.minZ * 16.0D) / 256.0D;
		double d4 = ((double)j + block.maxZ * 16.0D - 0.01D) / 256.0D;
		double d5 = ((double)(k + 16) - block.maxY * 16.0D) / 256.0D;
		double d6 = ((double)(k + 16) - block.minY * 16.0D - 0.01D) / 256.0D;
		double d8;
		if(this.flipTexture) {
			d8 = d3;
			d3 = d4;
			d4 = d8;
		}

		if(block.minZ < 0.0D || block.maxZ > 1.0D) {
			d3 = (double)(((float)j + 0.0F) / 256.0F);
			d4 = (double)(((float)j + 15.99F) / 256.0F);
		}

		if(block.minY < 0.0D || block.maxY > 1.0D) {
			d5 = (double)(((float)k + 0.0F) / 256.0F);
			d6 = (double)(((float)k + 15.99F) / 256.0F);
		}

		d8 = d4;
		double d9 = d3;
		double d10 = d5;
		double d11 = d6;
		if(this.field_31085_i == 2) {
			d3 = ((double)j + block.minY * 16.0D) / 256.0D;
			d5 = ((double)(k + 16) - block.minZ * 16.0D) / 256.0D;
			d4 = ((double)j + block.maxY * 16.0D) / 256.0D;
			d6 = ((double)(k + 16) - block.maxZ * 16.0D) / 256.0D;
			d10 = d5;
			d11 = d6;
			d8 = d3;
			d9 = d4;
			d5 = d6;
			d6 = d10;
		} else if(this.field_31085_i == 1) {
			d3 = ((double)(j + 16) - block.maxY * 16.0D) / 256.0D;
			d5 = ((double)k + block.maxZ * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.minY * 16.0D) / 256.0D;
			d6 = ((double)k + block.minZ * 16.0D) / 256.0D;
			d8 = d4;
			d9 = d3;
			d3 = d4;
			d4 = d9;
			d10 = d6;
			d11 = d5;
		} else if(this.field_31085_i == 3) {
			d3 = ((double)(j + 16) - block.minZ * 16.0D) / 256.0D;
			d4 = ((double)(j + 16) - block.maxZ * 16.0D - 0.01D) / 256.0D;
			d5 = ((double)k + block.maxY * 16.0D) / 256.0D;
			d6 = ((double)k + block.minY * 16.0D - 0.01D) / 256.0D;
			d8 = d4;
			d9 = d3;
			d10 = d5;
			d11 = d6;
		}

		double d12 = d + block.maxX;
		double d13 = d1 + block.minY;
		double d14 = d1 + block.maxY;
		double d15 = d2 + block.minZ;
		double d16 = d2 + block.maxZ;
		if(this.enableAO) {
			tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			tessellator.addVertexWithUV(d12, d13, d16, d9, d11);
			tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			tessellator.addVertexWithUV(d12, d14, d15, d8, d10);
			tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			tessellator.addVertexWithUV(d12, d14, d16, d3, d5);
		} else {
			tessellator.addVertexWithUV(d12, d13, d16, d9, d11);
			tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
			tessellator.addVertexWithUV(d12, d14, d15, d8, d10);
			tessellator.addVertexWithUV(d12, d14, d16, d3, d5);
		}

	}

	public void renderBlockOnInventory(Block block, int i, float f) {
		Tessellator tessellator = Tessellator.instance;
		int k;
		float i1;
		float f4;
		if(this.field_31088_b) {
			k = block.getRenderColor(i);
			i1 = (float)(k >> 16 & 255) / 255.0F;
			f4 = (float)(k >> 8 & 255) / 255.0F;
			float f5 = (float)(k & 255) / 255.0F;
			GL11.glColor4f(i1 * f, f4 * f, f5 * f, 1.0F);
		}

		k = block.getRenderType();
		if(k != 0 && k != 16) {
			if(k == 1) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderCrossedSquares(block, i, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if(k == 13) {
				block.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				i1 = 0.0625F;
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, -1.0F);
				tessellator.setTranslationF(0.0F, 0.0F, i1);
				this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
				tessellator.setTranslationF(0.0F, 0.0F, -i1);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator.setTranslationF(0.0F, 0.0F, -i1);
				this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
				tessellator.setTranslationF(0.0F, 0.0F, i1);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(-1.0F, 0.0F, 0.0F);
				tessellator.setTranslationF(i1, 0.0F, 0.0F);
				this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
				tessellator.setTranslationF(-i1, 0.0F, 0.0F);
				tessellator.draw();
				tessellator.startDrawingQuads();
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator.setTranslationF(-i1, 0.0F, 0.0F);
				this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
				tessellator.setTranslationF(i1, 0.0F, 0.0F);
				tessellator.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if(k == 6) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.func_1245_b(block, i, -0.5D, -0.5D, -0.5D);
				tessellator.draw();
			} else if(k == 2) {
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderTorchAtAngle(block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
				tessellator.draw();
			} else {
				int i9;
				if(k == 10) {
					for(i9 = 0; i9 < 2; ++i9) {
						if(i9 == 0) {
							block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
						}

						if(i9 == 1) {
							block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
						}

						GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
						tessellator.startDrawingQuads();
						tessellator.setNormal(0.0F, -1.0F, 0.0F);
						this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(0.0F, 1.0F, 0.0F);
						this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(0.0F, 0.0F, -1.0F);
						this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(0.0F, 0.0F, 1.0F);
						this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(-1.0F, 0.0F, 0.0F);
						this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(1.0F, 0.0F, 0.0F);
						this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
						tessellator.draw();
						GL11.glTranslatef(0.5F, 0.5F, 0.5F);
					}
				} else if(k == 11) {
					for(i9 = 0; i9 < 4; ++i9) {
						f4 = 0.125F;
						if(i9 == 0) {
							block.setBlockBounds(0.5F - f4, 0.0F, 0.0F, 0.5F + f4, 1.0F, f4 * 2.0F);
						}

						if(i9 == 1) {
							block.setBlockBounds(0.5F - f4, 0.0F, 1.0F - f4 * 2.0F, 0.5F + f4, 1.0F, 1.0F);
						}

						f4 = 0.0625F;
						if(i9 == 2) {
							block.setBlockBounds(0.5F - f4, 1.0F - f4 * 3.0F, -f4 * 2.0F, 0.5F + f4, 1.0F - f4, 1.0F + f4 * 2.0F);
						}

						if(i9 == 3) {
							block.setBlockBounds(0.5F - f4, 0.5F - f4 * 3.0F, -f4 * 2.0F, 0.5F + f4, 0.5F - f4, 1.0F + f4 * 2.0F);
						}

						GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
						tessellator.startDrawingQuads();
						tessellator.setNormal(0.0F, -1.0F, 0.0F);
						this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(0.0F, 1.0F, 0.0F);
						this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(0.0F, 0.0F, -1.0F);
						this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(0.0F, 0.0F, 1.0F);
						this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(-1.0F, 0.0F, 0.0F);
						this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
						tessellator.draw();
						tessellator.startDrawingQuads();
						tessellator.setNormal(1.0F, 0.0F, 0.0F);
						this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
						tessellator.draw();
						GL11.glTranslatef(0.5F, 0.5F, 0.5F);
					}

					block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				} else {
					ModLoader.RenderInvBlock(this, block, i, k);
				}
			}
		} else {
			if(k == 16) {
				i = 1;
			}

			block.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, i));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, i));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, i));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, i));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, i));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, i));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

	}

	public static boolean renderItemIn3d(int i) {
		return i == 0 ? true : (i == 13 ? true : (i == 10 ? true : (i == 11 ? true : ModLoader.RenderBlockIsItemFull3D(i))));
	}

	static {
		for(int i = 0; i < redstoneColors.length; ++i) {
			float f = (float)i / 15.0F;
			float f1 = f * 0.6F + 0.4F;
			if(i == 0) {
				f = 0.0F;
			}

			float f2 = f * f * 0.7F - 0.5F;
			float f3 = f * f * 0.6F - 0.7F;
			if(f2 < 0.0F) {
				f2 = 0.0F;
			}

			if(f3 < 0.0F) {
				f3 = 0.0F;
			}

			redstoneColors[i] = new float[]{f1, f2, f3};
		}

	}
}
