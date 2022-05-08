package net.minecraft.src;
//
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

	static {
		for(int i = 0; i < redstoneColors.length; ++i) {
			float j = (float)i / 15.0F;
			float red = j * 0.6F + 0.4F;
			if(i == 0) {
				j = 0.0F;
			}

			float green = j * j * 0.7F - 0.5F;
			float blue = j * j * 0.6F - 0.7F;
			if(green < 0.0F) {
				green = 0.0F;
			}

			if(blue < 0.0F) {
				blue = 0.0F;
			}

			redstoneColors[i] = new float[]{red, green, blue};
		}

	}

	public RenderBlocks(IBlockAccess xp1) {
		this();
		this.blockAccess = xp1;
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

	public void renderBlockUsingTexture(Block uu1, int i1, int j1, int k1, int l1) {
		this.overrideBlockTexture = l1;
		this.renderBlockByRenderType(uu1, i1, j1, k1);
		this.overrideBlockTexture = -1;
	}

	public void func_31075_a(Block uu1, int i1, int j1, int k1) {
		this.renderAllFaces = true;
		this.renderBlockByRenderType(uu1, i1, j1, k1);
		this.renderAllFaces = false;
	}

	public boolean renderBlockByRenderType(Block uu1, int i1, int j1, int k1) {
		int l1 = uu1.getRenderType();
		uu1.setBlockBoundsBasedOnState(this.blockAccess, i1, j1, k1);
		return l1 == 0 ? this.renderStandardBlock(uu1, i1, j1, k1) : (l1 == 4 ? this.renderBlockFluids(uu1, i1, j1, k1) : (l1 == 13 ? this.renderBlockCactus(uu1, i1, j1, k1) : (l1 == 1 ? this.renderBlockReed(uu1, i1, j1, k1) : (l1 == 6 ? this.renderBlockCrops(uu1, i1, j1, k1) : (l1 == 2 ? this.renderBlockTorch(uu1, i1, j1, k1) : (l1 == 3 ? this.renderBlockFire(uu1, i1, j1, k1) : (l1 == 5 ? this.renderBlockRedstoneWire(uu1, i1, j1, k1) : (l1 == 8 ? this.renderBlockLadder(uu1, i1, j1, k1) : (l1 == 7 ? this.renderBlockDoor(uu1, i1, j1, k1) : (l1 == 9 ? this.renderBlockMinecartTrack((BlockRail)uu1, i1, j1, k1) : (l1 == 10 ? this.renderBlockStairs(uu1, i1, j1, k1) : (l1 == 11 ? this.renderBlockFence(uu1, i1, j1, k1) : (l1 == 12 ? this.renderBlockLever(uu1, i1, j1, k1) : (l1 == 14 ? this.renderBlockBed(uu1, i1, j1, k1) : (l1 == 15 ? this.renderBlockRepeater(uu1, i1, j1, k1) : (l1 == 16 ? this.func_31074_b(uu1, i1, j1, k1, false) : (l1 == 17 ? this.func_31080_c(uu1, i1, j1, k1, true) : (l1 == 18 ? this.renderBlockCable(uu1, i1, j1, k1) : ModLoader.RenderWorldBlock(this, this.blockAccess, i1, j1, k1, uu1, l1)))))))))))))))))));
	}

	private boolean renderBlockCable(Block block, int i, int j, int k) {

		float f = 0.4f;
		float f1 = (1.0F - f) / 2.0F;
		block.setBlockBounds(f1, f1, f1, f1 + f, f1 + f, f1 + f);
		renderStandardBlock(block, i, j, k);
		boolean flag = blockAccess.getBlockId(i + 1, j, k) == mod_RetroStorage.relay.blockID ? true : false || blockAccess.getBlockId(i + 1, j, k) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i + 1, j, k) instanceof TileEntityInNetwork ? true : false;
		boolean flag1 = blockAccess.getBlockId(i - 1, j, k) == mod_RetroStorage.relay.blockID ? true : false || blockAccess.getBlockId(i - 1, j, k) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i - 1, j, k) instanceof TileEntityInNetwork ? true : false;
		boolean flag2 = blockAccess.getBlockId(i, j + 1, k) == mod_RetroStorage.relay.blockID ? true : false || blockAccess.getBlockId(i, j + 1, k) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i, j + 1, k) instanceof TileEntityInNetwork ? true : false;
		boolean flag3 = blockAccess.getBlockId(i, j - 1, k) == mod_RetroStorage.relay.blockID ? true : false || blockAccess.getBlockId(i, j - 1, k) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i, j - 1, k) instanceof TileEntityInNetwork ? true : false;
		boolean flag4 = blockAccess.getBlockId(i, j, k + 1) == mod_RetroStorage.relay.blockID ? true : false || blockAccess.getBlockId(i, j, k + 1) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i, j, k + 1) instanceof TileEntityInNetwork ? true : false;
		boolean flag5 = blockAccess.getBlockId(i, j, k - 1) == mod_RetroStorage.relay.blockID ? true : false || blockAccess.getBlockId(i , j, k - 1) == block.blockID ? true : false || blockAccess.getBlockTileEntity(i, j, k - 1) instanceof TileEntityInNetwork ? true : false;
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

	public boolean renderBlockBed(Block uu1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		int i2 = BlockBed.getDirectionFromMetadata(l1);
		boolean flag = BlockBed.isBlockFootOfBed(l1);
		float f1 = 0.5F;
		float f2 = 1.0F;
		float f3 = 0.8F;
		float f4 = 0.6F;
		float f17 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		nw1.setColorOpaque_F(f1 * f17, f1 * f17, f1 * f17);
		int i18 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 0);
		int j2 = (i18 & 15) << 4;
		int k2 = i18 & 240;
		double d1 = (double)((float)j2 / 256.0F);
		double d3 = ((double)(j2 + 16) - 0.01D) / 256.0D;
		double d5 = (double)((float)k2 / 256.0F);
		double d7 = ((double)(k2 + 16) - 0.01D) / 256.0D;
		double d9 = (double)i1 + uu1.minX;
		double d11 = (double)i1 + uu1.maxX;
		double d13 = (double)j1 + uu1.minY + 0.1875D;
		double d15 = (double)k1 + uu1.minZ;
		double d17 = (double)k1 + uu1.maxZ;
		nw1.addVertexWithUV(d9, d13, d17, d1, d7);
		nw1.addVertexWithUV(d9, d13, d15, d1, d5);
		nw1.addVertexWithUV(d11, d13, d15, d3, d5);
		nw1.addVertexWithUV(d11, d13, d17, d3, d7);
		float f18 = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
		nw1.setColorOpaque_F(f2 * f18, f2 * f18, f2 * f18);
		j2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 1);
		k2 = (j2 & 15) << 4;
		d1 = (double)(j2 & 240);
		double d2 = (double)((float)k2 / 256.0F);
		double d4 = ((double)(k2 + 16) - 0.01D) / 256.0D;
		double d6 = (double)((float)d1 / 256.0F);
		double d8 = (d1 + 16.0D - 0.01D) / 256.0D;
		double d10 = d2;
		double d12 = d4;
		double d14 = d6;
		double d16 = d6;
		double d18 = d2;
		double d19 = d4;
		double d20 = d8;
		double d21 = d8;
		if(i2 == 0) {
			d12 = d2;
			d14 = d8;
			d18 = d4;
			d21 = d6;
		} else if(i2 == 2) {
			d10 = d4;
			d16 = d8;
			d19 = d2;
			d20 = d6;
		} else if(i2 == 3) {
			d10 = d4;
			d16 = d8;
			d19 = d2;
			d20 = d6;
			d12 = d2;
			d14 = d8;
			d18 = d4;
			d21 = d6;
		}

		double d22 = (double)i1 + uu1.minX;
		double d23 = (double)i1 + uu1.maxX;
		double d24 = (double)j1 + uu1.maxY;
		double d25 = (double)k1 + uu1.minZ;
		double d26 = (double)k1 + uu1.maxZ;
		nw1.addVertexWithUV(d23, d24, d26, d18, d20);
		nw1.addVertexWithUV(d23, d24, d25, d10, d14);
		nw1.addVertexWithUV(d22, d24, d25, d12, d16);
		nw1.addVertexWithUV(d22, d24, d26, d19, d21);
		f18 = (float)ModelBed.field_22280_a[i2];
		if(flag) {
			f18 = (float)ModelBed.field_22280_a[ModelBed.field_22279_b[i2]];
		}

		byte j21 = 4;
		switch(i2) {
		case 0:
			j21 = 5;
			break;
		case 1:
			j21 = 3;
		case 2:
		default:
			break;
		case 3:
			j21 = 2;
		}

		float f22;
		if(f18 != 2.0F && (this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 - 1, 2))) {
			f22 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
			if(uu1.minZ > 0.0D) {
				f22 = f17;
			}

			nw1.setColorOpaque_F(f3 * f22, f3 * f22, f3 * f22);
			this.flipTexture = j21 == 2;
			this.renderEastFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 2));
		}

		if(f18 != 3.0F && (this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 + 1, 3))) {
			f22 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
			if(uu1.maxZ < 1.0D) {
				f22 = f17;
			}

			nw1.setColorOpaque_F(f3 * f22, f3 * f22, f3 * f22);
			this.flipTexture = j21 == 3;
			this.renderWestFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 3));
		}

		if(f18 != 4.0F && (this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1 - 1, j1, k1, 4))) {
			f22 = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
			if(uu1.minX > 0.0D) {
				f22 = f17;
			}

			nw1.setColorOpaque_F(f4 * f22, f4 * f22, f4 * f22);
			this.flipTexture = j21 == 4;
			this.renderNorthFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 4));
		}

		if(f18 != 5.0F && (this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1 + 1, j1, k1, 5))) {
			f22 = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
			if(uu1.maxX < 1.0D) {
				f22 = f17;
			}

			nw1.setColorOpaque_F(f4 * f22, f4 * f22, f4 * f22);
			this.flipTexture = j21 == 5;
			this.renderSouthFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 5));
		}

		this.flipTexture = false;
		return true;
	}

	public boolean renderBlockTorch(Block uu1, int i1, int j1, int k1) {
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		Tessellator nw1 = Tessellator.instance;
		float f1 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		if(Block.lightValue[uu1.blockID] > 0) {
			f1 = 1.0F;
		}

		nw1.setColorOpaque_F(f1, f1, f1);
		double d1 = (double)0.4F;
		double d2 = 0.5D - d1;
		double d3 = (double)0.2F;
		if(l1 == 1) {
			this.renderTorchAtAngle(uu1, (double)i1 - d2, (double)j1 + d3, (double)k1, -d1, 0.0D);
		} else if(l1 == 2) {
			this.renderTorchAtAngle(uu1, (double)i1 + d2, (double)j1 + d3, (double)k1, d1, 0.0D);
		} else if(l1 == 3) {
			this.renderTorchAtAngle(uu1, (double)i1, (double)j1 + d3, (double)k1 - d2, 0.0D, -d1);
		} else if(l1 == 4) {
			this.renderTorchAtAngle(uu1, (double)i1, (double)j1 + d3, (double)k1 + d2, 0.0D, d1);
		} else {
			this.renderTorchAtAngle(uu1, (double)i1, (double)j1, (double)k1, 0.0D, 0.0D);
		}

		return true;
	}

	public boolean renderBlockRepeater(Block uu1, int i1, int j1, int k1) {
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		int i2 = l1 & 3;
		int j2 = (l1 & 12) >> 2;
		this.renderStandardBlock(uu1, i1, j1, k1);
		Tessellator nw1 = Tessellator.instance;
		float f1 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		if(Block.lightValue[uu1.blockID] > 0) {
			f1 = (f1 + 1.0F) * 0.5F;
		}

		nw1.setColorOpaque_F(f1, f1, f1);
		double d1 = -0.1875D;
		double d2 = 0.0D;
		double d3 = 0.0D;
		double d4 = 0.0D;
		double d5 = 0.0D;
		switch(i2) {
		case 0:
			d5 = -0.3125D;
			d3 = BlockRedstoneRepeater.field_22024_a[j2];
			break;
		case 1:
			d4 = 0.3125D;
			d2 = -BlockRedstoneRepeater.field_22024_a[j2];
			break;
		case 2:
			d5 = 0.3125D;
			d3 = -BlockRedstoneRepeater.field_22024_a[j2];
			break;
		case 3:
			d4 = -0.3125D;
			d2 = BlockRedstoneRepeater.field_22024_a[j2];
		}

		this.renderTorchAtAngle(uu1, (double)i1 + d2, (double)j1 + d1, (double)k1 + d3, 0.0D, 0.0D);
		this.renderTorchAtAngle(uu1, (double)i1 + d4, (double)j1 + d1, (double)k1 + d5, 0.0D, 0.0D);
		int k2 = uu1.getBlockTextureFromSide(1);
		int l2 = (k2 & 15) << 4;
		int i3 = k2 & 240;
		double d6 = (double)((float)l2 / 256.0F);
		double d7 = (double)(((float)l2 + 15.99F) / 256.0F);
		double d8 = (double)((float)i3 / 256.0F);
		double d9 = (double)(((float)i3 + 15.99F) / 256.0F);
		float f2 = 0.125F;
		float f3 = (float)(i1 + 1);
		float f4 = (float)(i1 + 1);
		float f5 = (float)(i1 + 0);
		float f6 = (float)(i1 + 0);
		float f7 = (float)(k1 + 0);
		float f8 = (float)(k1 + 1);
		float f9 = (float)(k1 + 1);
		float f10 = (float)(k1 + 0);
		float f11 = (float)j1 + f2;
		if(i2 == 2) {
			f3 = f4 = (float)(i1 + 0);
			f5 = f6 = (float)(i1 + 1);
			f7 = f10 = (float)(k1 + 1);
			f8 = f9 = (float)(k1 + 0);
		} else if(i2 == 3) {
			f3 = f6 = (float)(i1 + 0);
			f4 = f5 = (float)(i1 + 1);
			f7 = f8 = (float)(k1 + 0);
			f9 = f10 = (float)(k1 + 1);
		} else if(i2 == 1) {
			f3 = f6 = (float)(i1 + 1);
			f4 = f5 = (float)(i1 + 0);
			f7 = f8 = (float)(k1 + 1);
			f9 = f10 = (float)(k1 + 0);
		}

		nw1.addVertexWithUV((double)f6, (double)f11, (double)f10, d6, d8);
		nw1.addVertexWithUV((double)f5, (double)f11, (double)f9, d6, d9);
		nw1.addVertexWithUV((double)f4, (double)f11, (double)f8, d7, d9);
		nw1.addVertexWithUV((double)f3, (double)f11, (double)f7, d7, d8);
		return true;
	}

	public void func_31078_d(Block uu1, int i1, int j1, int k1) {
		this.renderAllFaces = true;
		this.func_31074_b(uu1, i1, j1, k1, true);
		this.renderAllFaces = false;
	}

	public boolean func_31074_b(Block uu1, int i1, int j1, int k1, boolean flag) {
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		boolean flag1 = flag || (l1 & 8) != 0;
		int i2 = BlockPistonBase.func_31044_d(l1);
		if(flag1) {
			switch(i2) {
			case 0:
				this.field_31087_g = 3;
				this.field_31086_h = 3;
				this.field_31085_i = 3;
				this.field_31084_j = 3;
				uu1.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 1:
				uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
				break;
			case 2:
				this.field_31085_i = 1;
				this.field_31084_j = 2;
				uu1.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
				break;
			case 3:
				this.field_31085_i = 2;
				this.field_31084_j = 1;
				this.field_31083_k = 3;
				this.field_31082_l = 3;
				uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
				break;
			case 4:
				this.field_31087_g = 1;
				this.field_31086_h = 2;
				this.field_31083_k = 2;
				this.field_31082_l = 1;
				uu1.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 5:
				this.field_31087_g = 2;
				this.field_31086_h = 1;
				this.field_31083_k = 1;
				this.field_31082_l = 2;
				uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
			}

			this.renderStandardBlock(uu1, i1, j1, k1);
			this.field_31087_g = 0;
			this.field_31086_h = 0;
			this.field_31085_i = 0;
			this.field_31084_j = 0;
			this.field_31083_k = 0;
			this.field_31082_l = 0;
			uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			switch(i2) {
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

			this.renderStandardBlock(uu1, i1, j1, k1);
			this.field_31087_g = 0;
			this.field_31086_h = 0;
			this.field_31085_i = 0;
			this.field_31084_j = 0;
			this.field_31083_k = 0;
			this.field_31082_l = 0;
		}

		return true;
	}

	public void func_31076_a(double d1, double d2, double d3, double d4, double d5, double d6, float f1, double d7) {
		int i1 = 108;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		Tessellator nw1 = Tessellator.instance;
		double d8 = (double)((float)(j1 + 0) / 256.0F);
		double d9 = (double)((float)(k1 + 0) / 256.0F);
		double d10 = ((double)j1 + d7 - 0.01D) / 256.0D;
		double d11 = ((double)((float)k1 + 4.0F) - 0.01D) / 256.0D;
		nw1.setColorOpaque_F(f1, f1, f1);
		nw1.addVertexWithUV(d1, d4, d5, d10, d9);
		nw1.addVertexWithUV(d1, d3, d5, d8, d9);
		nw1.addVertexWithUV(d2, d3, d6, d8, d11);
		nw1.addVertexWithUV(d2, d4, d6, d10, d11);
	}

	public void func_31081_b(double d1, double d2, double d3, double d4, double d5, double d6, float f1, double d7) {
		int i1 = 108;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		Tessellator nw1 = Tessellator.instance;
		double d8 = (double)((float)(j1 + 0) / 256.0F);
		double d9 = (double)((float)(k1 + 0) / 256.0F);
		double d10 = ((double)j1 + d7 - 0.01D) / 256.0D;
		double d11 = ((double)((float)k1 + 4.0F) - 0.01D) / 256.0D;
		nw1.setColorOpaque_F(f1, f1, f1);
		nw1.addVertexWithUV(d1, d3, d6, d10, d9);
		nw1.addVertexWithUV(d1, d3, d5, d8, d9);
		nw1.addVertexWithUV(d2, d4, d5, d8, d11);
		nw1.addVertexWithUV(d2, d4, d6, d10, d11);
	}

	public void func_31077_c(double d1, double d2, double d3, double d4, double d5, double d6, float f1, double d7) {
		int i1 = 108;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		Tessellator nw1 = Tessellator.instance;
		double d8 = (double)((float)(j1 + 0) / 256.0F);
		double d9 = (double)((float)(k1 + 0) / 256.0F);
		double d10 = ((double)j1 + d7 - 0.01D) / 256.0D;
		double d11 = ((double)((float)k1 + 4.0F) - 0.01D) / 256.0D;
		nw1.setColorOpaque_F(f1, f1, f1);
		nw1.addVertexWithUV(d2, d3, d5, d10, d9);
		nw1.addVertexWithUV(d1, d3, d5, d8, d9);
		nw1.addVertexWithUV(d1, d4, d6, d8, d11);
		nw1.addVertexWithUV(d2, d4, d6, d10, d11);
	}

	public void func_31079_a(Block uu1, int i1, int j1, int k1, boolean flag) {
		this.renderAllFaces = true;
		this.func_31080_c(uu1, i1, j1, k1, flag);
		this.renderAllFaces = false;
	}

	public boolean func_31080_c(Block uu1, int i1, int j1, int k1, boolean flag) {
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		int i2 = BlockPistonExtension.func_31050_c(l1);
		float f1 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		float f2 = flag ? 1.0F : 0.5F;
		double d1 = flag ? 16.0D : 8.0D;
		switch(i2) {
		case 0:
			this.field_31087_g = 3;
			this.field_31086_h = 3;
			this.field_31085_i = 3;
			this.field_31084_j = 3;
			uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			this.func_31076_a((double)((float)i1 + 0.375F), (double)((float)i1 + 0.625F), (double)((float)j1 + 0.25F), (double)((float)j1 + 0.25F + f2), (double)((float)k1 + 0.625F), (double)((float)k1 + 0.625F), f1 * 0.8F, d1);
			this.func_31076_a((double)((float)i1 + 0.625F), (double)((float)i1 + 0.375F), (double)((float)j1 + 0.25F), (double)((float)j1 + 0.25F + f2), (double)((float)k1 + 0.375F), (double)((float)k1 + 0.375F), f1 * 0.8F, d1);
			this.func_31076_a((double)((float)i1 + 0.375F), (double)((float)i1 + 0.375F), (double)((float)j1 + 0.25F), (double)((float)j1 + 0.25F + f2), (double)((float)k1 + 0.375F), (double)((float)k1 + 0.625F), f1 * 0.6F, d1);
			this.func_31076_a((double)((float)i1 + 0.625F), (double)((float)i1 + 0.625F), (double)((float)j1 + 0.25F), (double)((float)j1 + 0.25F + f2), (double)((float)k1 + 0.625F), (double)((float)k1 + 0.375F), f1 * 0.6F, d1);
			break;
		case 1:
			uu1.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			this.func_31076_a((double)((float)i1 + 0.375F), (double)((float)i1 + 0.625F), (double)((float)j1 - 0.25F + 1.0F - f2), (double)((float)j1 - 0.25F + 1.0F), (double)((float)k1 + 0.625F), (double)((float)k1 + 0.625F), f1 * 0.8F, d1);
			this.func_31076_a((double)((float)i1 + 0.625F), (double)((float)i1 + 0.375F), (double)((float)j1 - 0.25F + 1.0F - f2), (double)((float)j1 - 0.25F + 1.0F), (double)((float)k1 + 0.375F), (double)((float)k1 + 0.375F), f1 * 0.8F, d1);
			this.func_31076_a((double)((float)i1 + 0.375F), (double)((float)i1 + 0.375F), (double)((float)j1 - 0.25F + 1.0F - f2), (double)((float)j1 - 0.25F + 1.0F), (double)((float)k1 + 0.375F), (double)((float)k1 + 0.625F), f1 * 0.6F, d1);
			this.func_31076_a((double)((float)i1 + 0.625F), (double)((float)i1 + 0.625F), (double)((float)j1 - 0.25F + 1.0F - f2), (double)((float)j1 - 0.25F + 1.0F), (double)((float)k1 + 0.625F), (double)((float)k1 + 0.375F), f1 * 0.6F, d1);
			break;
		case 2:
			this.field_31085_i = 1;
			this.field_31084_j = 2;
			uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			this.func_31081_b((double)((float)i1 + 0.375F), (double)((float)i1 + 0.375F), (double)((float)j1 + 0.625F), (double)((float)j1 + 0.375F), (double)((float)k1 + 0.25F), (double)((float)k1 + 0.25F + f2), f1 * 0.6F, d1);
			this.func_31081_b((double)((float)i1 + 0.625F), (double)((float)i1 + 0.625F), (double)((float)j1 + 0.375F), (double)((float)j1 + 0.625F), (double)((float)k1 + 0.25F), (double)((float)k1 + 0.25F + f2), f1 * 0.6F, d1);
			this.func_31081_b((double)((float)i1 + 0.375F), (double)((float)i1 + 0.625F), (double)((float)j1 + 0.375F), (double)((float)j1 + 0.375F), (double)((float)k1 + 0.25F), (double)((float)k1 + 0.25F + f2), f1 * 0.5F, d1);
			this.func_31081_b((double)((float)i1 + 0.625F), (double)((float)i1 + 0.375F), (double)((float)j1 + 0.625F), (double)((float)j1 + 0.625F), (double)((float)k1 + 0.25F), (double)((float)k1 + 0.25F + f2), f1, d1);
			break;
		case 3:
			this.field_31085_i = 2;
			this.field_31084_j = 1;
			this.field_31083_k = 3;
			this.field_31082_l = 3;
			uu1.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			this.func_31081_b((double)((float)i1 + 0.375F), (double)((float)i1 + 0.375F), (double)((float)j1 + 0.625F), (double)((float)j1 + 0.375F), (double)((float)k1 - 0.25F + 1.0F - f2), (double)((float)k1 - 0.25F + 1.0F), f1 * 0.6F, d1);
			this.func_31081_b((double)((float)i1 + 0.625F), (double)((float)i1 + 0.625F), (double)((float)j1 + 0.375F), (double)((float)j1 + 0.625F), (double)((float)k1 - 0.25F + 1.0F - f2), (double)((float)k1 - 0.25F + 1.0F), f1 * 0.6F, d1);
			this.func_31081_b((double)((float)i1 + 0.375F), (double)((float)i1 + 0.625F), (double)((float)j1 + 0.375F), (double)((float)j1 + 0.375F), (double)((float)k1 - 0.25F + 1.0F - f2), (double)((float)k1 - 0.25F + 1.0F), f1 * 0.5F, d1);
			this.func_31081_b((double)((float)i1 + 0.625F), (double)((float)i1 + 0.375F), (double)((float)j1 + 0.625F), (double)((float)j1 + 0.625F), (double)((float)k1 - 0.25F + 1.0F - f2), (double)((float)k1 - 0.25F + 1.0F), f1, d1);
			break;
		case 4:
			this.field_31087_g = 1;
			this.field_31086_h = 2;
			this.field_31083_k = 2;
			this.field_31082_l = 1;
			uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			this.func_31077_c((double)((float)i1 + 0.25F), (double)((float)i1 + 0.25F + f2), (double)((float)j1 + 0.375F), (double)((float)j1 + 0.375F), (double)((float)k1 + 0.625F), (double)((float)k1 + 0.375F), f1 * 0.5F, d1);
			this.func_31077_c((double)((float)i1 + 0.25F), (double)((float)i1 + 0.25F + f2), (double)((float)j1 + 0.625F), (double)((float)j1 + 0.625F), (double)((float)k1 + 0.375F), (double)((float)k1 + 0.625F), f1, d1);
			this.func_31077_c((double)((float)i1 + 0.25F), (double)((float)i1 + 0.25F + f2), (double)((float)j1 + 0.375F), (double)((float)j1 + 0.625F), (double)((float)k1 + 0.375F), (double)((float)k1 + 0.375F), f1 * 0.6F, d1);
			this.func_31077_c((double)((float)i1 + 0.25F), (double)((float)i1 + 0.25F + f2), (double)((float)j1 + 0.625F), (double)((float)j1 + 0.375F), (double)((float)k1 + 0.625F), (double)((float)k1 + 0.625F), f1 * 0.6F, d1);
			break;
		case 5:
			this.field_31087_g = 2;
			this.field_31086_h = 1;
			this.field_31083_k = 1;
			this.field_31082_l = 2;
			uu1.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			this.func_31077_c((double)((float)i1 - 0.25F + 1.0F - f2), (double)((float)i1 - 0.25F + 1.0F), (double)((float)j1 + 0.375F), (double)((float)j1 + 0.375F), (double)((float)k1 + 0.625F), (double)((float)k1 + 0.375F), f1 * 0.5F, d1);
			this.func_31077_c((double)((float)i1 - 0.25F + 1.0F - f2), (double)((float)i1 - 0.25F + 1.0F), (double)((float)j1 + 0.625F), (double)((float)j1 + 0.625F), (double)((float)k1 + 0.375F), (double)((float)k1 + 0.625F), f1, d1);
			this.func_31077_c((double)((float)i1 - 0.25F + 1.0F - f2), (double)((float)i1 - 0.25F + 1.0F), (double)((float)j1 + 0.375F), (double)((float)j1 + 0.625F), (double)((float)k1 + 0.375F), (double)((float)k1 + 0.375F), f1 * 0.6F, d1);
			this.func_31077_c((double)((float)i1 - 0.25F + 1.0F - f2), (double)((float)i1 - 0.25F + 1.0F), (double)((float)j1 + 0.625F), (double)((float)j1 + 0.375F), (double)((float)k1 + 0.625F), (double)((float)k1 + 0.625F), f1 * 0.6F, d1);
		}

		this.field_31087_g = 0;
		this.field_31086_h = 0;
		this.field_31085_i = 0;
		this.field_31084_j = 0;
		this.field_31083_k = 0;
		this.field_31082_l = 0;
		uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}

	public boolean renderBlockLever(Block uu1, int i1, int j1, int k1) {
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		int i2 = l1 & 7;
		boolean flag = (l1 & 8) > 0;
		Tessellator nw1 = Tessellator.instance;
		boolean flag1 = this.overrideBlockTexture >= 0;
		if(!flag1) {
			this.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
		}

		float f1 = 0.25F;
		float f2 = 0.1875F;
		float f3 = 0.1875F;
		if(i2 == 5) {
			uu1.setBlockBounds(0.5F - f2, 0.0F, 0.5F - f1, 0.5F + f2, f3, 0.5F + f1);
		} else if(i2 == 6) {
			uu1.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f2, 0.5F + f1, f3, 0.5F + f2);
		} else if(i2 == 4) {
			uu1.setBlockBounds(0.5F - f2, 0.5F - f1, 1.0F - f3, 0.5F + f2, 0.5F + f1, 1.0F);
		} else if(i2 == 3) {
			uu1.setBlockBounds(0.5F - f2, 0.5F - f1, 0.0F, 0.5F + f2, 0.5F + f1, f3);
		} else if(i2 == 2) {
			uu1.setBlockBounds(1.0F - f3, 0.5F - f1, 0.5F - f2, 1.0F, 0.5F + f1, 0.5F + f2);
		} else if(i2 == 1) {
			uu1.setBlockBounds(0.0F, 0.5F - f1, 0.5F - f2, f3, 0.5F + f1, 0.5F + f2);
		}

		this.renderStandardBlock(uu1, i1, j1, k1);
		if(!flag1) {
			this.overrideBlockTexture = -1;
		}

		float f4 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		if(Block.lightValue[uu1.blockID] > 0) {
			f4 = 1.0F;
		}

		nw1.setColorOpaque_F(f4, f4, f4);
		int j2 = uu1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			j2 = this.overrideBlockTexture;
		}

		int k2 = (j2 & 15) << 4;
		int l2 = j2 & 240;
		float f5 = (float)k2 / 256.0F;
		float f6 = ((float)k2 + 15.99F) / 256.0F;
		float f7 = (float)l2 / 256.0F;
		float f8 = ((float)l2 + 15.99F) / 256.0F;
		Vec3D[] abt = new Vec3D[8];
		float f9 = 0.0625F;
		float f10 = 0.0625F;
		float f11 = 0.625F;
		abt[0] = Vec3D.createVector((double)(-f9), 0.0D, (double)(-f10));
		abt[1] = Vec3D.createVector((double)f9, 0.0D, (double)(-f10));
		abt[2] = Vec3D.createVector((double)f9, 0.0D, (double)f10);
		abt[3] = Vec3D.createVector((double)(-f9), 0.0D, (double)f10);
		abt[4] = Vec3D.createVector((double)(-f9), (double)f11, (double)(-f10));
		abt[5] = Vec3D.createVector((double)f9, (double)f11, (double)(-f10));
		abt[6] = Vec3D.createVector((double)f9, (double)f11, (double)f10);
		abt[7] = Vec3D.createVector((double)(-f9), (double)f11, (double)f10);

		for(int bt1 = 0; bt1 < 8; ++bt1) {
			if(flag) {
				abt[bt1].zCoord -= 0.0625D;
				abt[bt1].rotateAroundX(0.6981317F);
			} else {
				abt[bt1].zCoord += 0.0625D;
				abt[bt1].rotateAroundX(-0.6981317F);
			}

			if(i2 == 6) {
				abt[bt1].rotateAroundY(1.570796F);
			}

			if(i2 < 5) {
				abt[bt1].yCoord -= 0.375D;
				abt[bt1].rotateAroundX(1.570796F);
				if(i2 == 4) {
					abt[bt1].rotateAroundY(0.0F);
				}

				if(i2 == 3) {
					abt[bt1].rotateAroundY(3.141593F);
				}

				if(i2 == 2) {
					abt[bt1].rotateAroundY(1.570796F);
				}

				if(i2 == 1) {
					abt[bt1].rotateAroundY(-1.570796F);
				}

				abt[bt1].xCoord += (double)i1 + 0.5D;
				abt[bt1].yCoord += (double)((float)j1 + 0.5F);
				abt[bt1].zCoord += (double)k1 + 0.5D;
			} else {
				abt[bt1].xCoord += (double)i1 + 0.5D;
				abt[bt1].yCoord += (double)((float)j1 + 0.125F);
				abt[bt1].zCoord += (double)k1 + 0.5D;
			}
		}

		Vec3D vec3D30 = null;
		Vec3D bt2 = null;
		Vec3D bt3 = null;
		Vec3D bt4 = null;

		for(int j3 = 0; j3 < 6; ++j3) {
			if(j3 == 0) {
				f5 = (float)(k2 + 7) / 256.0F;
				f6 = ((float)(k2 + 9) - 0.01F) / 256.0F;
				f7 = (float)(l2 + 6) / 256.0F;
				f8 = ((float)(l2 + 8) - 0.01F) / 256.0F;
			} else if(j3 == 2) {
				f5 = (float)(k2 + 7) / 256.0F;
				f6 = ((float)(k2 + 9) - 0.01F) / 256.0F;
				f7 = (float)(l2 + 6) / 256.0F;
				f8 = ((float)(l2 + 16) - 0.01F) / 256.0F;
			}

			if(j3 == 0) {
				vec3D30 = abt[0];
				bt2 = abt[1];
				bt3 = abt[2];
				bt4 = abt[3];
			} else if(j3 == 1) {
				vec3D30 = abt[7];
				bt2 = abt[6];
				bt3 = abt[5];
				bt4 = abt[4];
			} else if(j3 == 2) {
				vec3D30 = abt[1];
				bt2 = abt[0];
				bt3 = abt[4];
				bt4 = abt[5];
			} else if(j3 == 3) {
				vec3D30 = abt[2];
				bt2 = abt[1];
				bt3 = abt[5];
				bt4 = abt[6];
			} else if(j3 == 4) {
				vec3D30 = abt[3];
				bt2 = abt[2];
				bt3 = abt[6];
				bt4 = abt[7];
			} else if(j3 == 5) {
				vec3D30 = abt[0];
				bt2 = abt[3];
				bt3 = abt[7];
				bt4 = abt[4];
			}

			nw1.addVertexWithUV(vec3D30.xCoord, vec3D30.yCoord, vec3D30.zCoord, (double)f5, (double)f8);
			nw1.addVertexWithUV(bt2.xCoord, bt2.yCoord, bt2.zCoord, (double)f6, (double)f8);
			nw1.addVertexWithUV(bt3.xCoord, bt3.yCoord, bt3.zCoord, (double)f6, (double)f7);
			nw1.addVertexWithUV(bt4.xCoord, bt4.yCoord, bt4.zCoord, (double)f5, (double)f7);
		}

		return true;
	}

	public boolean renderBlockFire(Block uu1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		int l1 = uu1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			l1 = this.overrideBlockTexture;
		}

		float f1 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		nw1.setColorOpaque_F(f1, f1, f1);
		int i2 = (l1 & 15) << 4;
		int j2 = l1 & 240;
		double d1 = (double)((float)i2 / 256.0F);
		double d3 = (double)(((float)i2 + 15.99F) / 256.0F);
		double d5 = (double)((float)j2 / 256.0F);
		double d7 = (double)(((float)j2 + 15.99F) / 256.0F);
		float f2 = 1.4F;
		double d12;
		double d14;
		double d16;
		double d18;
		double d20;
		double d22;
		double d24;
		if(!this.blockAccess.isBlockNormalCube(i1, j1 - 1, k1) && !Block.fire.canBlockCatchFire(this.blockAccess, i1, j1 - 1, k1)) {
			float f46 = 0.2F;
			float f5 = 0.0625F;
			if((i1 + j1 + k1 & 1) == 1) {
				d1 = (double)((float)i2 / 256.0F);
				d3 = (double)(((float)i2 + 15.99F) / 256.0F);
				d5 = (double)((float)(j2 + 16) / 256.0F);
				d7 = (double)(((float)j2 + 15.99F + 16.0F) / 256.0F);
			}

			if((i1 / 2 + j1 / 2 + k1 / 2 & 1) == 1) {
				d12 = d3;
				d3 = d1;
				d1 = d12;
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i1 - 1, j1, k1)) {
				nw1.addVertexWithUV((double)((float)i1 + f46), (double)((float)j1 + f2 + f5), (double)(k1 + 1), d3, d5);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 1), d3, d7);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 0), d1, d7);
				nw1.addVertexWithUV((double)((float)i1 + f46), (double)((float)j1 + f2 + f5), (double)(k1 + 0), d1, d5);
				nw1.addVertexWithUV((double)((float)i1 + f46), (double)((float)j1 + f2 + f5), (double)(k1 + 0), d1, d5);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 0), d1, d7);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 1), d3, d7);
				nw1.addVertexWithUV((double)((float)i1 + f46), (double)((float)j1 + f2 + f5), (double)(k1 + 1), d3, d5);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i1 + 1, j1, k1)) {
				nw1.addVertexWithUV((double)((float)(i1 + 1) - f46), (double)((float)j1 + f2 + f5), (double)(k1 + 0), d1, d5);
				nw1.addVertexWithUV((double)(i1 + 1 - 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 0), d1, d7);
				nw1.addVertexWithUV((double)(i1 + 1 - 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 1), d3, d7);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - f46), (double)((float)j1 + f2 + f5), (double)(k1 + 1), d3, d5);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - f46), (double)((float)j1 + f2 + f5), (double)(k1 + 1), d3, d5);
				nw1.addVertexWithUV((double)(i1 + 1 - 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 1), d3, d7);
				nw1.addVertexWithUV((double)(i1 + 1 - 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 0), d1, d7);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - f46), (double)((float)j1 + f2 + f5), (double)(k1 + 0), d1, d5);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i1, j1, k1 - 1)) {
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f2 + f5), (double)((float)k1 + f46), d3, d5);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 0), d3, d7);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)(j1 + 0) + f5), (double)(k1 + 0), d1, d7);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f2 + f5), (double)((float)k1 + f46), d1, d5);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f2 + f5), (double)((float)k1 + f46), d1, d5);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)(j1 + 0) + f5), (double)(k1 + 0), d1, d7);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 0), d3, d7);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f2 + f5), (double)((float)k1 + f46), d3, d5);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i1, j1, k1 + 1)) {
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f2 + f5), (double)((float)(k1 + 1) - f46), d1, d5);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)(j1 + 0) + f5), (double)(k1 + 1 - 0), d1, d7);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 1 - 0), d3, d7);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f2 + f5), (double)((float)(k1 + 1) - f46), d3, d5);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f2 + f5), (double)((float)(k1 + 1) - f46), d3, d5);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 0) + f5), (double)(k1 + 1 - 0), d3, d7);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)(j1 + 0) + f5), (double)(k1 + 1 - 0), d1, d7);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f2 + f5), (double)((float)(k1 + 1) - f46), d1, d5);
			}

			if(Block.fire.canBlockCatchFire(this.blockAccess, i1, j1 + 1, k1)) {
				d12 = (double)i1 + 0.5D + 0.5D;
				d14 = (double)i1 + 0.5D - 0.5D;
				d16 = (double)k1 + 0.5D + 0.5D;
				d18 = (double)k1 + 0.5D - 0.5D;
				d20 = (double)i1 + 0.5D - 0.5D;
				d22 = (double)i1 + 0.5D + 0.5D;
				d24 = (double)k1 + 0.5D - 0.5D;
				double d25 = (double)k1 + 0.5D + 0.5D;
				double d2 = (double)((float)i2 / 256.0F);
				double d4 = (double)(((float)i2 + 15.99F) / 256.0F);
				double d6 = (double)((float)j2 / 256.0F);
				double d8 = (double)(((float)j2 + 15.99F) / 256.0F);
				++j1;
				float f3 = -0.2F;
				if((i1 + j1 + k1 & 1) == 0) {
					nw1.addVertexWithUV(d20, (double)((float)j1 + f3), (double)(k1 + 0), d4, d6);
					nw1.addVertexWithUV(d12, (double)(j1 + 0), (double)(k1 + 0), d4, d8);
					nw1.addVertexWithUV(d12, (double)(j1 + 0), (double)(k1 + 1), d2, d8);
					nw1.addVertexWithUV(d20, (double)((float)j1 + f3), (double)(k1 + 1), d2, d6);
					d2 = (double)((float)i2 / 256.0F);
					d4 = (double)(((float)i2 + 15.99F) / 256.0F);
					d6 = (double)((float)(j2 + 16) / 256.0F);
					d8 = (double)(((float)j2 + 15.99F + 16.0F) / 256.0F);
					nw1.addVertexWithUV(d22, (double)((float)j1 + f3), (double)(k1 + 1), d4, d6);
					nw1.addVertexWithUV(d14, (double)(j1 + 0), (double)(k1 + 1), d4, d8);
					nw1.addVertexWithUV(d14, (double)(j1 + 0), (double)(k1 + 0), d2, d8);
					nw1.addVertexWithUV(d22, (double)((float)j1 + f3), (double)(k1 + 0), d2, d6);
				} else {
					nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f3), d25, d4, d6);
					nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), d18, d4, d8);
					nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), d18, d2, d8);
					nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f3), d25, d2, d6);
					d2 = (double)((float)i2 / 256.0F);
					d4 = (double)(((float)i2 + 15.99F) / 256.0F);
					d6 = (double)((float)(j2 + 16) / 256.0F);
					d8 = (double)(((float)j2 + 15.99F + 16.0F) / 256.0F);
					nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f3), d24, d4, d6);
					nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), d16, d4, d8);
					nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), d16, d2, d8);
					nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f3), d24, d2, d6);
				}
			}
		} else {
			double f4 = (double)i1 + 0.5D + 0.2D;
			d12 = (double)i1 + 0.5D - 0.2D;
			d14 = (double)k1 + 0.5D + 0.2D;
			d16 = (double)k1 + 0.5D - 0.2D;
			d18 = (double)i1 + 0.5D - 0.3D;
			d20 = (double)i1 + 0.5D + 0.3D;
			d22 = (double)k1 + 0.5D - 0.3D;
			d24 = (double)k1 + 0.5D + 0.3D;
			nw1.addVertexWithUV(d18, (double)((float)j1 + f2), (double)(k1 + 1), d3, d5);
			nw1.addVertexWithUV(f4, (double)(j1 + 0), (double)(k1 + 1), d3, d7);
			nw1.addVertexWithUV(f4, (double)(j1 + 0), (double)(k1 + 0), d1, d7);
			nw1.addVertexWithUV(d18, (double)((float)j1 + f2), (double)(k1 + 0), d1, d5);
			nw1.addVertexWithUV(d20, (double)((float)j1 + f2), (double)(k1 + 0), d3, d5);
			nw1.addVertexWithUV(d12, (double)(j1 + 0), (double)(k1 + 0), d3, d7);
			nw1.addVertexWithUV(d12, (double)(j1 + 0), (double)(k1 + 1), d1, d7);
			nw1.addVertexWithUV(d20, (double)((float)j1 + f2), (double)(k1 + 1), d1, d5);
			d1 = (double)((float)i2 / 256.0F);
			d3 = (double)(((float)i2 + 15.99F) / 256.0F);
			d5 = (double)((float)(j2 + 16) / 256.0F);
			d7 = (double)(((float)j2 + 15.99F + 16.0F) / 256.0F);
			nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f2), d24, d3, d5);
			nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), d16, d3, d7);
			nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), d16, d1, d7);
			nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f2), d24, d1, d5);
			nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f2), d22, d3, d5);
			nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), d14, d3, d7);
			nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), d14, d1, d7);
			nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f2), d22, d1, d5);
			f4 = (double)i1 + 0.5D - 0.5D;
			d12 = (double)i1 + 0.5D + 0.5D;
			d14 = (double)k1 + 0.5D - 0.5D;
			d16 = (double)k1 + 0.5D + 0.5D;
			d18 = (double)i1 + 0.5D - 0.4D;
			d20 = (double)i1 + 0.5D + 0.4D;
			d22 = (double)k1 + 0.5D - 0.4D;
			d24 = (double)k1 + 0.5D + 0.4D;
			nw1.addVertexWithUV(d18, (double)((float)j1 + f2), (double)(k1 + 0), d1, d5);
			nw1.addVertexWithUV(f4, (double)(j1 + 0), (double)(k1 + 0), d1, d7);
			nw1.addVertexWithUV(f4, (double)(j1 + 0), (double)(k1 + 1), d3, d7);
			nw1.addVertexWithUV(d18, (double)((float)j1 + f2), (double)(k1 + 1), d3, d5);
			nw1.addVertexWithUV(d20, (double)((float)j1 + f2), (double)(k1 + 1), d1, d5);
			nw1.addVertexWithUV(d12, (double)(j1 + 0), (double)(k1 + 1), d1, d7);
			nw1.addVertexWithUV(d12, (double)(j1 + 0), (double)(k1 + 0), d3, d7);
			nw1.addVertexWithUV(d20, (double)((float)j1 + f2), (double)(k1 + 0), d3, d5);
			d1 = (double)((float)i2 / 256.0F);
			d3 = (double)(((float)i2 + 15.99F) / 256.0F);
			d5 = (double)((float)j2 / 256.0F);
			d7 = (double)(((float)j2 + 15.99F) / 256.0F);
			nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f2), d24, d1, d5);
			nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), d16, d1, d7);
			nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), d16, d3, d7);
			nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f2), d24, d3, d5);
			nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f2), d22, d1, d5);
			nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), d14, d1, d7);
			nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), d14, d3, d7);
			nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f2), d22, d3, d5);
		}

		return true;
	}

	public static void setRedstoneColors(float[][] colors) {
		if(colors.length != 16) {
			throw new IllegalArgumentException("Must be 16 colors.");
		} else {
			for(int i = 0; i < colors.length; ++i) {
				if(colors[i].length != 3) {
					throw new IllegalArgumentException("Must be 3 channels in a color.");
				}
			}

			redstoneColors = colors;
		}
	}

	public boolean renderBlockRedstoneWire(Block uu1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		int i2 = uu1.getBlockTextureFromSideAndMetadata(1, l1);
		if(this.overrideBlockTexture >= 0) {
			i2 = this.overrideBlockTexture;
		}

		float f1 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		float[] color = redstoneColors[l1];
		float f3 = color[0];
		float f4 = color[1];
		float f5 = color[2];
		nw1.setColorOpaque_F(f1 * f3, f1 * f4, f1 * f5);
		int j2 = (i2 & 15) << 4;
		int k2 = i2 & 240;
		double d1 = (double)((float)j2 / 256.0F);
		double d3 = (double)(((float)j2 + 15.99F) / 256.0F);
		double d5 = (double)((float)k2 / 256.0F);
		double d7 = (double)(((float)k2 + 15.99F) / 256.0F);
		boolean flag = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1 - 1, j1, k1, 1) || !this.blockAccess.isBlockNormalCube(i1 - 1, j1, k1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1 - 1, j1 - 1, k1, -1);
		boolean flag1 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1 + 1, j1, k1, 3) || !this.blockAccess.isBlockNormalCube(i1 + 1, j1, k1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1 + 1, j1 - 1, k1, -1);
		boolean flag2 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1, j1, k1 - 1, 2) || !this.blockAccess.isBlockNormalCube(i1, j1, k1 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1, j1 - 1, k1 - 1, -1);
		boolean flag3 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1, j1, k1 + 1, 0) || !this.blockAccess.isBlockNormalCube(i1, j1, k1 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1, j1 - 1, k1 + 1, -1);
		if(!this.blockAccess.isBlockNormalCube(i1, j1 + 1, k1)) {
			if(this.blockAccess.isBlockNormalCube(i1 - 1, j1, k1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1 - 1, j1 + 1, k1, -1)) {
				flag = true;
			}

			if(this.blockAccess.isBlockNormalCube(i1 + 1, j1, k1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1 + 1, j1 + 1, k1, -1)) {
				flag1 = true;
			}

			if(this.blockAccess.isBlockNormalCube(i1, j1, k1 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1, j1 + 1, k1 - 1, -1)) {
				flag2 = true;
			}

			if(this.blockAccess.isBlockNormalCube(i1, j1, k1 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, i1, j1 + 1, k1 + 1, -1)) {
				flag3 = true;
			}
		}

		float f6 = (float)(i1 + 0);
		float f7 = (float)(i1 + 1);
		float f8 = (float)(k1 + 0);
		float f9 = (float)(k1 + 1);
		byte byte0 = 0;
		if((flag || flag1) && !flag2 && !flag3) {
			byte0 = 1;
		}

		if((flag2 || flag3) && !flag1 && !flag) {
			byte0 = 2;
		}

		if(byte0 != 0) {
			d1 = (double)((float)(j2 + 16) / 256.0F);
			d3 = (double)(((float)(j2 + 16) + 15.99F) / 256.0F);
			d5 = (double)((float)k2 / 256.0F);
			d7 = (double)(((float)k2 + 15.99F) / 256.0F);
		}

		if(byte0 == 0) {
			if(flag1 || flag2 || flag3 || flag) {
				if(!flag) {
					f6 += 0.3125F;
				}

				if(!flag) {
					d1 += 0.01953125D;
				}

				if(!flag1) {
					f7 -= 0.3125F;
				}

				if(!flag1) {
					d3 -= 0.01953125D;
				}

				if(!flag2) {
					f8 += 0.3125F;
				}

				if(!flag2) {
					d5 += 0.01953125D;
				}

				if(!flag3) {
					f9 -= 0.3125F;
				}

				if(!flag3) {
					d7 -= 0.01953125D;
				}
			}

			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f9, d3, d7);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f8, d3, d5);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f8, d1, d5);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f9, d1, d7);
			nw1.setColorOpaque_F(f1, f1, f1);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f9, d3, d7 + 0.0625D);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f8, d3, d5 + 0.0625D);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f8, d1, d5 + 0.0625D);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f9, d1, d7 + 0.0625D);
		} else if(byte0 == 1) {
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f9, d3, d7);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f8, d3, d5);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f8, d1, d5);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f9, d1, d7);
			nw1.setColorOpaque_F(f1, f1, f1);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f9, d3, d7 + 0.0625D);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f8, d3, d5 + 0.0625D);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f8, d1, d5 + 0.0625D);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f9, d1, d7 + 0.0625D);
		} else if(byte0 == 2) {
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f9, d3, d7);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f8, d1, d7);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f8, d1, d5);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f9, d3, d5);
			nw1.setColorOpaque_F(f1, f1, f1);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f9, d3, d7 + 0.0625D);
			nw1.addVertexWithUV((double)f7, (double)((float)j1 + 0.015625F), (double)f8, d1, d7 + 0.0625D);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f8, d1, d5 + 0.0625D);
			nw1.addVertexWithUV((double)f6, (double)((float)j1 + 0.015625F), (double)f9, d3, d5 + 0.0625D);
		}

		if(!this.blockAccess.isBlockNormalCube(i1, j1 + 1, k1)) {
			double d2 = (double)((float)(j2 + 16) / 256.0F);
			double d4 = (double)(((float)(j2 + 16) + 15.99F) / 256.0F);
			double d6 = (double)((float)k2 / 256.0F);
			double d8 = (double)(((float)k2 + 15.99F) / 256.0F);
			if(this.blockAccess.isBlockNormalCube(i1 - 1, j1, k1) && this.blockAccess.getBlockId(i1 - 1, j1 + 1, k1) == Block.redstoneWire.blockID) {
				nw1.setColorOpaque_F(f1 * f3, f1 * f4, f1 * f5);
				nw1.addVertexWithUV((double)((float)i1 + 0.015625F), (double)((float)(j1 + 1) + 0.021875F), (double)(k1 + 1), d4, d6);
				nw1.addVertexWithUV((double)((float)i1 + 0.015625F), (double)(j1 + 0), (double)(k1 + 1), d2, d6);
				nw1.addVertexWithUV((double)((float)i1 + 0.015625F), (double)(j1 + 0), (double)(k1 + 0), d2, d8);
				nw1.addVertexWithUV((double)((float)i1 + 0.015625F), (double)((float)(j1 + 1) + 0.021875F), (double)(k1 + 0), d4, d8);
				nw1.setColorOpaque_F(f1, f1, f1);
				nw1.addVertexWithUV((double)((float)i1 + 0.015625F), (double)((float)(j1 + 1) + 0.021875F), (double)(k1 + 1), d4, d6 + 0.0625D);
				nw1.addVertexWithUV((double)((float)i1 + 0.015625F), (double)(j1 + 0), (double)(k1 + 1), d2, d6 + 0.0625D);
				nw1.addVertexWithUV((double)((float)i1 + 0.015625F), (double)(j1 + 0), (double)(k1 + 0), d2, d8 + 0.0625D);
				nw1.addVertexWithUV((double)((float)i1 + 0.015625F), (double)((float)(j1 + 1) + 0.021875F), (double)(k1 + 0), d4, d8 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(i1 + 1, j1, k1) && this.blockAccess.getBlockId(i1 + 1, j1 + 1, k1) == Block.redstoneWire.blockID) {
				nw1.setColorOpaque_F(f1 * f3, f1 * f4, f1 * f5);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - 0.015625F), (double)(j1 + 0), (double)(k1 + 1), d2, d8);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - 0.015625F), (double)((float)(j1 + 1) + 0.021875F), (double)(k1 + 1), d4, d8);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - 0.015625F), (double)((float)(j1 + 1) + 0.021875F), (double)(k1 + 0), d4, d6);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - 0.015625F), (double)(j1 + 0), (double)(k1 + 0), d2, d6);
				nw1.setColorOpaque_F(f1, f1, f1);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - 0.015625F), (double)(j1 + 0), (double)(k1 + 1), d2, d8 + 0.0625D);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - 0.015625F), (double)((float)(j1 + 1) + 0.021875F), (double)(k1 + 1), d4, d8 + 0.0625D);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - 0.015625F), (double)((float)(j1 + 1) + 0.021875F), (double)(k1 + 0), d4, d6 + 0.0625D);
				nw1.addVertexWithUV((double)((float)(i1 + 1) - 0.015625F), (double)(j1 + 0), (double)(k1 + 0), d2, d6 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(i1, j1, k1 - 1) && this.blockAccess.getBlockId(i1, j1 + 1, k1 - 1) == Block.redstoneWire.blockID) {
				nw1.setColorOpaque_F(f1 * f3, f1 * f4, f1 * f5);
				nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), (double)((float)k1 + 0.015625F), d2, d8);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)(j1 + 1) + 0.021875F), (double)((float)k1 + 0.015625F), d4, d8);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 1) + 0.021875F), (double)((float)k1 + 0.015625F), d4, d6);
				nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), (double)((float)k1 + 0.015625F), d2, d6);
				nw1.setColorOpaque_F(f1, f1, f1);
				nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), (double)((float)k1 + 0.015625F), d2, d8 + 0.0625D);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)(j1 + 1) + 0.021875F), (double)((float)k1 + 0.015625F), d4, d8 + 0.0625D);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 1) + 0.021875F), (double)((float)k1 + 0.015625F), d4, d6 + 0.0625D);
				nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), (double)((float)k1 + 0.015625F), d2, d6 + 0.0625D);
			}

			if(this.blockAccess.isBlockNormalCube(i1, j1, k1 + 1) && this.blockAccess.getBlockId(i1, j1 + 1, k1 + 1) == Block.redstoneWire.blockID) {
				nw1.setColorOpaque_F(f1 * f3, f1 * f4, f1 * f5);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)(j1 + 1) + 0.021875F), (double)((float)(k1 + 1) - 0.015625F), d4, d6);
				nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), (double)((float)(k1 + 1) - 0.015625F), d2, d6);
				nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), (double)((float)(k1 + 1) - 0.015625F), d2, d8);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 1) + 0.021875F), (double)((float)(k1 + 1) - 0.015625F), d4, d8);
				nw1.setColorOpaque_F(f1, f1, f1);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)(j1 + 1) + 0.021875F), (double)((float)(k1 + 1) - 0.015625F), d4, d6 + 0.0625D);
				nw1.addVertexWithUV((double)(i1 + 1), (double)(j1 + 0), (double)((float)(k1 + 1) - 0.015625F), d2, d6 + 0.0625D);
				nw1.addVertexWithUV((double)(i1 + 0), (double)(j1 + 0), (double)((float)(k1 + 1) - 0.015625F), d2, d8 + 0.0625D);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)(j1 + 1) + 0.021875F), (double)((float)(k1 + 1) - 0.015625F), d4, d8 + 0.0625D);
			}
		}

		return true;
	}

	public boolean renderBlockMinecartTrack(BlockRail pc1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		int i2 = pc1.getBlockTextureFromSideAndMetadata(0, l1);
		if(this.overrideBlockTexture >= 0) {
			i2 = this.overrideBlockTexture;
		}

		if(pc1.getIsPowered()) {
			l1 &= 7;
		}

		float f1 = pc1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		nw1.setColorOpaque_F(f1, f1, f1);
		int j2 = (i2 & 15) << 4;
		int k2 = i2 & 240;
		double d1 = (double)((float)j2 / 256.0F);
		double d2 = (double)(((float)j2 + 15.99F) / 256.0F);
		double d3 = (double)((float)k2 / 256.0F);
		double d4 = (double)(((float)k2 + 15.99F) / 256.0F);
		float f2 = 0.0625F;
		float f3 = (float)(i1 + 1);
		float f4 = (float)(i1 + 1);
		float f5 = (float)(i1 + 0);
		float f6 = (float)(i1 + 0);
		float f7 = (float)(k1 + 0);
		float f8 = (float)(k1 + 1);
		float f9 = (float)(k1 + 1);
		float f10 = (float)(k1 + 0);
		float f11 = (float)j1 + f2;
		float f12 = (float)j1 + f2;
		float f13 = (float)j1 + f2;
		float f14 = (float)j1 + f2;
		if(l1 != 1 && l1 != 2 && l1 != 3 && l1 != 7) {
			if(l1 == 8) {
				f3 = f4 = (float)(i1 + 0);
				f5 = f6 = (float)(i1 + 1);
				f7 = f10 = (float)(k1 + 1);
				f8 = f9 = (float)(k1 + 0);
			} else if(l1 == 9) {
				f3 = f6 = (float)(i1 + 0);
				f4 = f5 = (float)(i1 + 1);
				f7 = f8 = (float)(k1 + 0);
				f9 = f10 = (float)(k1 + 1);
			}
		} else {
			f3 = f6 = (float)(i1 + 1);
			f4 = f5 = (float)(i1 + 0);
			f7 = f8 = (float)(k1 + 1);
			f9 = f10 = (float)(k1 + 0);
		}

		if(l1 != 2 && l1 != 4) {
			if(l1 == 3 || l1 == 5) {
				++f12;
				++f13;
			}
		} else {
			++f11;
			++f14;
		}

		nw1.addVertexWithUV((double)f3, (double)f11, (double)f7, d2, d3);
		nw1.addVertexWithUV((double)f4, (double)f12, (double)f8, d2, d4);
		nw1.addVertexWithUV((double)f5, (double)f13, (double)f9, d1, d4);
		nw1.addVertexWithUV((double)f6, (double)f14, (double)f10, d1, d3);
		nw1.addVertexWithUV((double)f6, (double)f14, (double)f10, d1, d3);
		nw1.addVertexWithUV((double)f5, (double)f13, (double)f9, d1, d4);
		nw1.addVertexWithUV((double)f4, (double)f12, (double)f8, d2, d4);
		nw1.addVertexWithUV((double)f3, (double)f11, (double)f7, d2, d3);
		return true;
	}

	public boolean renderBlockLadder(Block uu1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		int l1 = uu1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			l1 = this.overrideBlockTexture;
		}

		float f1 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		nw1.setColorOpaque_F(f1, f1, f1);
		int i2 = (l1 & 15) << 4;
		int j2 = l1 & 240;
		double d1 = (double)((float)i2 / 256.0F);
		double d2 = (double)(((float)i2 + 15.99F) / 256.0F);
		double d3 = (double)((float)j2 / 256.0F);
		double d4 = (double)(((float)j2 + 15.99F) / 256.0F);
		int k2 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		float f2 = 0.0F;
		float f3 = 0.05F;
		if(k2 == 5) {
			nw1.addVertexWithUV((double)((float)i1 + f3), (double)((float)(j1 + 1) + f2), (double)((float)(k1 + 1) + f2), d1, d3);
			nw1.addVertexWithUV((double)((float)i1 + f3), (double)((float)(j1 + 0) - f2), (double)((float)(k1 + 1) + f2), d1, d4);
			nw1.addVertexWithUV((double)((float)i1 + f3), (double)((float)(j1 + 0) - f2), (double)((float)(k1 + 0) - f2), d2, d4);
			nw1.addVertexWithUV((double)((float)i1 + f3), (double)((float)(j1 + 1) + f2), (double)((float)(k1 + 0) - f2), d2, d3);
		}

		if(k2 == 4) {
			nw1.addVertexWithUV((double)((float)(i1 + 1) - f3), (double)((float)(j1 + 0) - f2), (double)((float)(k1 + 1) + f2), d2, d4);
			nw1.addVertexWithUV((double)((float)(i1 + 1) - f3), (double)((float)(j1 + 1) + f2), (double)((float)(k1 + 1) + f2), d2, d3);
			nw1.addVertexWithUV((double)((float)(i1 + 1) - f3), (double)((float)(j1 + 1) + f2), (double)((float)(k1 + 0) - f2), d1, d3);
			nw1.addVertexWithUV((double)((float)(i1 + 1) - f3), (double)((float)(j1 + 0) - f2), (double)((float)(k1 + 0) - f2), d1, d4);
		}

		if(k2 == 3) {
			nw1.addVertexWithUV((double)((float)(i1 + 1) + f2), (double)((float)(j1 + 0) - f2), (double)((float)k1 + f3), d2, d4);
			nw1.addVertexWithUV((double)((float)(i1 + 1) + f2), (double)((float)(j1 + 1) + f2), (double)((float)k1 + f3), d2, d3);
			nw1.addVertexWithUV((double)((float)(i1 + 0) - f2), (double)((float)(j1 + 1) + f2), (double)((float)k1 + f3), d1, d3);
			nw1.addVertexWithUV((double)((float)(i1 + 0) - f2), (double)((float)(j1 + 0) - f2), (double)((float)k1 + f3), d1, d4);
		}

		if(k2 == 2) {
			nw1.addVertexWithUV((double)((float)(i1 + 1) + f2), (double)((float)(j1 + 1) + f2), (double)((float)(k1 + 1) - f3), d1, d3);
			nw1.addVertexWithUV((double)((float)(i1 + 1) + f2), (double)((float)(j1 + 0) - f2), (double)((float)(k1 + 1) - f3), d1, d4);
			nw1.addVertexWithUV((double)((float)(i1 + 0) - f2), (double)((float)(j1 + 0) - f2), (double)((float)(k1 + 1) - f3), d2, d4);
			nw1.addVertexWithUV((double)((float)(i1 + 0) - f2), (double)((float)(j1 + 1) + f2), (double)((float)(k1 + 1) - f3), d2, d3);
		}

		return true;
	}

	public boolean renderBlockReed(Block uu1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		float f1 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		int l1 = uu1.colorMultiplier(this.blockAccess, i1, j1, k1);
		float f2 = (float)(l1 >> 16 & 255) / 255.0F;
		float f3 = (float)(l1 >> 8 & 255) / 255.0F;
		float f4 = (float)(l1 & 255) / 255.0F;
		if(EntityRenderer.field_28135_a) {
			float d1 = (f2 * 30.0F + f3 * 59.0F + f4 * 11.0F) / 100.0F;
			float f6 = (f2 * 30.0F + f3 * 70.0F) / 100.0F;
			float d2 = (f2 * 30.0F + f4 * 70.0F) / 100.0F;
			f2 = d1;
			f3 = f6;
			f4 = d2;
		}

		nw1.setColorOpaque_F(f1 * f2, f1 * f3, f1 * f4);
		double d11 = (double)i1;
		double d21 = (double)j1;
		double d3 = (double)k1;
		if(uu1 == Block.tallGrass) {
			long l2 = (long)(i1 * 3129871) ^ (long)k1 * 116129781L ^ (long)j1;
			l2 = l2 * l2 * 42317861L + l2 * 11L;
			d11 += ((double)((float)(l2 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
			d21 += ((double)((float)(l2 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
			d3 += ((double)((float)(l2 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
		}

		this.renderCrossedSquares(uu1, this.blockAccess.getBlockMetadata(i1, j1, k1), d11, d21, d3);
		return true;
	}

	public boolean renderBlockCrops(Block uu1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		float f1 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		nw1.setColorOpaque_F(f1, f1, f1);
		this.func_1245_b(uu1, this.blockAccess.getBlockMetadata(i1, j1, k1), (double)i1, (double)j1 - 0.0625D, (double)k1);
		return true;
	}

	public void renderTorchAtAngle(Block uu1, double d1, double d2, double d3, double d4, double d5) {
		Tessellator nw1 = Tessellator.instance;
		int i1 = uu1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		float f1 = (float)j1 / 256.0F;
		float f2 = ((float)j1 + 15.99F) / 256.0F;
		float f3 = (float)k1 / 256.0F;
		float f4 = ((float)k1 + 15.99F) / 256.0F;
		double d6 = (double)f1 + 7.0D / 256D;
		double d7 = (double)f3 + 6.0D / 256D;
		double d8 = (double)f1 + 9.0D / 256D;
		double d9 = (double)f3 + 8.0D / 256D;
		d1 += 0.5D;
		d3 += 0.5D;
		double d10 = d1 - 0.5D;
		double d11 = d1 + 0.5D;
		double d12 = d3 - 0.5D;
		double d13 = d3 + 0.5D;
		double d14 = 0.0625D;
		double d15 = 0.625D;
		nw1.addVertexWithUV(d1 + d4 * (1.0D - d15) - d14, d2 + d15, d3 + d5 * (1.0D - d15) - d14, d6, d7);
		nw1.addVertexWithUV(d1 + d4 * (1.0D - d15) - d14, d2 + d15, d3 + d5 * (1.0D - d15) + d14, d6, d9);
		nw1.addVertexWithUV(d1 + d4 * (1.0D - d15) + d14, d2 + d15, d3 + d5 * (1.0D - d15) + d14, d8, d9);
		nw1.addVertexWithUV(d1 + d4 * (1.0D - d15) + d14, d2 + d15, d3 + d5 * (1.0D - d15) - d14, d8, d7);
		nw1.addVertexWithUV(d1 - d14, d2 + 1.0D, d12, (double)f1, (double)f3);
		nw1.addVertexWithUV(d1 - d14 + d4, d2 + 0.0D, d12 + d5, (double)f1, (double)f4);
		nw1.addVertexWithUV(d1 - d14 + d4, d2 + 0.0D, d13 + d5, (double)f2, (double)f4);
		nw1.addVertexWithUV(d1 - d14, d2 + 1.0D, d13, (double)f2, (double)f3);
		nw1.addVertexWithUV(d1 + d14, d2 + 1.0D, d13, (double)f1, (double)f3);
		nw1.addVertexWithUV(d1 + d4 + d14, d2 + 0.0D, d13 + d5, (double)f1, (double)f4);
		nw1.addVertexWithUV(d1 + d4 + d14, d2 + 0.0D, d12 + d5, (double)f2, (double)f4);
		nw1.addVertexWithUV(d1 + d14, d2 + 1.0D, d12, (double)f2, (double)f3);
		nw1.addVertexWithUV(d10, d2 + 1.0D, d3 + d14, (double)f1, (double)f3);
		nw1.addVertexWithUV(d10 + d4, d2 + 0.0D, d3 + d14 + d5, (double)f1, (double)f4);
		nw1.addVertexWithUV(d11 + d4, d2 + 0.0D, d3 + d14 + d5, (double)f2, (double)f4);
		nw1.addVertexWithUV(d11, d2 + 1.0D, d3 + d14, (double)f2, (double)f3);
		nw1.addVertexWithUV(d11, d2 + 1.0D, d3 - d14, (double)f1, (double)f3);
		nw1.addVertexWithUV(d11 + d4, d2 + 0.0D, d3 - d14 + d5, (double)f1, (double)f4);
		nw1.addVertexWithUV(d10 + d4, d2 + 0.0D, d3 - d14 + d5, (double)f2, (double)f4);
		nw1.addVertexWithUV(d10, d2 + 1.0D, d3 - d14, (double)f2, (double)f3);
	}

	public void renderCrossedSquares(Block uu1, int i1, double d1, double d2, double d3) {
		Tessellator nw1 = Tessellator.instance;
		int j1 = uu1.getBlockTextureFromSideAndMetadata(0, i1);
		if(this.overrideBlockTexture >= 0) {
			j1 = this.overrideBlockTexture;
		}

		int k1 = (j1 & 15) << 4;
		int l1 = j1 & 240;
		double d4 = (double)((float)k1 / 256.0F);
		double d5 = (double)(((float)k1 + 15.99F) / 256.0F);
		double d6 = (double)((float)l1 / 256.0F);
		double d7 = (double)(((float)l1 + 15.99F) / 256.0F);
		double d8 = d1 + 0.5D - (double)0.45F;
		double d9 = d1 + 0.5D + (double)0.45F;
		double d10 = d3 + 0.5D - (double)0.45F;
		double d11 = d3 + 0.5D + (double)0.45F;
		nw1.addVertexWithUV(d8, d2 + 1.0D, d10, d4, d6);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d10, d4, d7);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d11, d5, d7);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d11, d5, d6);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d11, d4, d6);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d11, d4, d7);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d10, d5, d7);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d10, d5, d6);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d11, d4, d6);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d11, d4, d7);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d10, d5, d7);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d10, d5, d6);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d10, d4, d6);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d10, d4, d7);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d11, d5, d7);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d11, d5, d6);
	}

	public void func_1245_b(Block uu1, int i1, double d1, double d2, double d3) {
		Tessellator nw1 = Tessellator.instance;
		int j1 = uu1.getBlockTextureFromSideAndMetadata(0, i1);
		if(this.overrideBlockTexture >= 0) {
			j1 = this.overrideBlockTexture;
		}

		int k1 = (j1 & 15) << 4;
		int l1 = j1 & 240;
		double d4 = (double)((float)k1 / 256.0F);
		double d5 = (double)(((float)k1 + 15.99F) / 256.0F);
		double d6 = (double)((float)l1 / 256.0F);
		double d7 = (double)(((float)l1 + 15.99F) / 256.0F);
		double d8 = d1 + 0.5D - 0.25D;
		double d9 = d1 + 0.5D + 0.25D;
		double d10 = d3 + 0.5D - 0.5D;
		double d11 = d3 + 0.5D + 0.5D;
		nw1.addVertexWithUV(d8, d2 + 1.0D, d10, d4, d6);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d10, d4, d7);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d11, d5, d7);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d11, d5, d6);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d11, d4, d6);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d11, d4, d7);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d10, d5, d7);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d10, d5, d6);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d11, d4, d6);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d11, d4, d7);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d10, d5, d7);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d10, d5, d6);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d10, d4, d6);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d10, d4, d7);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d11, d5, d7);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d11, d5, d6);
		d8 = d1 + 0.5D - 0.5D;
		d9 = d1 + 0.5D + 0.5D;
		d10 = d3 + 0.5D - 0.25D;
		d11 = d3 + 0.5D + 0.25D;
		nw1.addVertexWithUV(d8, d2 + 1.0D, d10, d4, d6);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d10, d4, d7);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d10, d5, d7);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d10, d5, d6);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d10, d4, d6);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d10, d4, d7);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d10, d5, d7);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d10, d5, d6);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d11, d4, d6);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d11, d4, d7);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d11, d5, d7);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d11, d5, d6);
		nw1.addVertexWithUV(d8, d2 + 1.0D, d11, d4, d6);
		nw1.addVertexWithUV(d8, d2 + 0.0D, d11, d4, d7);
		nw1.addVertexWithUV(d9, d2 + 0.0D, d11, d5, d7);
		nw1.addVertexWithUV(d9, d2 + 1.0D, d11, d5, d6);
	}

	public boolean renderBlockFluids(Block uu1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		int l1 = uu1.colorMultiplier(this.blockAccess, i1, j1, k1);
		float f1 = (float)(l1 >> 16 & 255) / 255.0F;
		float f2 = (float)(l1 >> 8 & 255) / 255.0F;
		float f3 = (float)(l1 & 255) / 255.0F;
		boolean flag = uu1.shouldSideBeRendered(this.blockAccess, i1, j1 + 1, k1, 1);
		boolean flag1 = uu1.shouldSideBeRendered(this.blockAccess, i1, j1 - 1, k1, 0);
		boolean[] aflag = new boolean[]{uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 - 1, 2), uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 + 1, 3), uu1.shouldSideBeRendered(this.blockAccess, i1 - 1, j1, k1, 4), uu1.shouldSideBeRendered(this.blockAccess, i1 + 1, j1, k1, 5)};
		if(!flag && !flag1 && !aflag[0] && !aflag[1] && !aflag[2] && !aflag[3]) {
			return false;
		} else {
			boolean flag2 = false;
			float f4 = 0.5F;
			float f5 = 1.0F;
			float f6 = 0.8F;
			float f7 = 0.6F;
			double d1 = 0.0D;
			double d2 = 1.0D;
			Material ln1 = uu1.blockMaterial;
			int i2 = this.blockAccess.getBlockMetadata(i1, j1, k1);
			float f8 = this.func_1224_a(i1, j1, k1, ln1);
			float f9 = this.func_1224_a(i1, j1, k1 + 1, ln1);
			float f10 = this.func_1224_a(i1 + 1, j1, k1 + 1, ln1);
			float f11 = this.func_1224_a(i1 + 1, j1, k1, ln1);
			int k2;
			int l3;
			float f16;
			float f18;
			float f20;
			if(this.renderAllFaces || flag) {
				flag2 = true;
				k2 = uu1.getBlockTextureFromSideAndMetadata(1, i2);
				float l2 = (float)BlockFluid.func_293_a(this.blockAccess, i1, j1, k1, ln1);
				if(l2 > -999.0F) {
					k2 = uu1.getBlockTextureFromSideAndMetadata(2, i2);
				}

				int j3 = (k2 & 15) << 4;
				l3 = k2 & 240;
				double i4 = ((double)j3 + 8.0D) / 256.0D;
				double k4 = ((double)l3 + 8.0D) / 256.0D;
				if(l2 < -999.0F) {
					l2 = 0.0F;
				} else {
					i4 = (double)((float)(j3 + 16) / 256.0F);
					k4 = (double)((float)(l3 + 16) / 256.0F);
				}

				f16 = MathHelper.sin(l2) * 8.0F / 256.0F;
				f18 = MathHelper.cos(l2) * 8.0F / 256.0F;
				f20 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
				nw1.setColorOpaque_F(f5 * f20 * f1, f5 * f20 * f2, f5 * f20 * f3);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f8), (double)(k1 + 0), i4 - (double)f18 - (double)f16, k4 - (double)f18 + (double)f16);
				nw1.addVertexWithUV((double)(i1 + 0), (double)((float)j1 + f9), (double)(k1 + 1), i4 - (double)f18 + (double)f16, k4 + (double)f18 + (double)f16);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f10), (double)(k1 + 1), i4 + (double)f18 + (double)f16, k4 + (double)f18 - (double)f16);
				nw1.addVertexWithUV((double)(i1 + 1), (double)((float)j1 + f11), (double)(k1 + 0), i4 + (double)f18 - (double)f16, k4 - (double)f18 - (double)f16);
			}

			if(this.renderAllFaces || flag1) {
				float f52 = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
				nw1.setColorOpaque_F(f4 * f52, f4 * f52, f4 * f52);
				this.renderBottomFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTextureFromSide(0));
				flag2 = true;
			}

			for(k2 = 0; k2 < 4; ++k2) {
				int i53 = i1;
				l3 = k1;
				if(k2 == 0) {
					l3 = k1 - 1;
				}

				if(k2 == 1) {
					++l3;
				}

				if(k2 == 2) {
					i53 = i1 - 1;
				}

				if(k2 == 3) {
					++i53;
				}

				int i54 = uu1.getBlockTextureFromSideAndMetadata(k2 + 2, i2);
				int j4 = (i54 & 15) << 4;
				int i55 = i54 & 240;
				if(this.renderAllFaces || aflag[k2]) {
					float f14;
					float f21;
					float f22;
					if(k2 == 0) {
						f14 = f8;
						f16 = f11;
						f18 = (float)i1;
						f21 = (float)(i1 + 1);
						f20 = (float)k1;
						f22 = (float)k1;
					} else if(k2 == 1) {
						f14 = f10;
						f16 = f9;
						f18 = (float)(i1 + 1);
						f21 = (float)i1;
						f20 = (float)(k1 + 1);
						f22 = (float)(k1 + 1);
					} else if(k2 == 2) {
						f14 = f9;
						f16 = f8;
						f18 = (float)i1;
						f21 = (float)i1;
						f20 = (float)(k1 + 1);
						f22 = (float)k1;
					} else {
						f14 = f11;
						f16 = f10;
						f18 = (float)(i1 + 1);
						f21 = (float)(i1 + 1);
						f20 = (float)k1;
						f22 = (float)(k1 + 1);
					}

					flag2 = true;
					double d5 = (double)((float)(j4 + 0) / 256.0F);
					double d6 = ((double)(j4 + 16) - 0.01D) / 256.0D;
					double d7 = (double)(((float)i55 + (1.0F - f14) * 16.0F) / 256.0F);
					double d8 = (double)(((float)i55 + (1.0F - f16) * 16.0F) / 256.0F);
					double d9 = ((double)(i55 + 16) - 0.01D) / 256.0D;
					float f23 = uu1.getBlockBrightness(this.blockAccess, i53, j1, l3);
					if(k2 < 2) {
						f23 *= f6;
					} else {
						f23 *= f7;
					}

					nw1.setColorOpaque_F(f5 * f23 * f1, f5 * f23 * f2, f5 * f23 * f3);
					nw1.addVertexWithUV((double)f18, (double)((float)j1 + f14), (double)f20, d5, d7);
					nw1.addVertexWithUV((double)f21, (double)((float)j1 + f16), (double)f22, d6, d8);
					nw1.addVertexWithUV((double)f21, (double)(j1 + 0), (double)f22, d6, d9);
					nw1.addVertexWithUV((double)f18, (double)(j1 + 0), (double)f20, d5, d9);
				}
			}

			uu1.minY = d1;
			uu1.maxY = d2;
			return flag2;
		}
	}

	public float func_1224_a(int i1, int j1, int k1, Material ln1) {
		int l1 = 0;
		float f1 = 0.0F;

		for(int i2 = 0; i2 < 4; ++i2) {
			int j2 = i1 - (i2 & 1);
			int l2 = k1 - (i2 >> 1 & 1);
			if(this.blockAccess.getBlockMaterial(j2, j1 + 1, l2) == ln1) {
				return 1.0F;
			}

			Material ln2 = this.blockAccess.getBlockMaterial(j2, j1, l2);
			if(ln2 == ln1) {
				int i3 = this.blockAccess.getBlockMetadata(j2, j1, l2);
				if(i3 >= 8 || i3 == 0) {
					f1 += BlockFluid.getPercentAir(i3) * 10.0F;
					l1 += 10;
				}

				f1 += BlockFluid.getPercentAir(i3);
				++l1;
			} else if(!ln2.isSolid()) {
				++f1;
				++l1;
			}
		}

		return 1.0F - f1 / (float)l1;
	}

	public void renderBlockFallingSand(Block uu1, World fd, int i1, int j1, int k1) {
		float f1 = 0.5F;
		float f2 = 1.0F;
		float f3 = 0.8F;
		float f4 = 0.6F;
		Tessellator nw1 = Tessellator.instance;
		nw1.startDrawingQuads();
		float f5 = uu1.getBlockBrightness(fd, i1, j1, k1);
		float f6 = uu1.getBlockBrightness(fd, i1, j1 - 1, k1);
		if(f6 < f5) {
			f6 = f5;
		}

		nw1.setColorOpaque_F(f1 * f6, f1 * f6, f1 * f6);
		this.renderBottomFace(uu1, -0.5D, -0.5D, -0.5D, uu1.getBlockTextureFromSide(0));
		f6 = uu1.getBlockBrightness(fd, i1, j1 + 1, k1);
		if(f6 < f5) {
			f6 = f5;
		}

		nw1.setColorOpaque_F(f2 * f6, f2 * f6, f2 * f6);
		this.renderTopFace(uu1, -0.5D, -0.5D, -0.5D, uu1.getBlockTextureFromSide(1));
		f6 = uu1.getBlockBrightness(fd, i1, j1, k1 - 1);
		if(f6 < f5) {
			f6 = f5;
		}

		nw1.setColorOpaque_F(f3 * f6, f3 * f6, f3 * f6);
		this.renderEastFace(uu1, -0.5D, -0.5D, -0.5D, uu1.getBlockTextureFromSide(2));
		f6 = uu1.getBlockBrightness(fd, i1, j1, k1 + 1);
		if(f6 < f5) {
			f6 = f5;
		}

		nw1.setColorOpaque_F(f3 * f6, f3 * f6, f3 * f6);
		this.renderWestFace(uu1, -0.5D, -0.5D, -0.5D, uu1.getBlockTextureFromSide(3));
		f6 = uu1.getBlockBrightness(fd, i1 - 1, j1, k1);
		if(f6 < f5) {
			f6 = f5;
		}

		nw1.setColorOpaque_F(f4 * f6, f4 * f6, f4 * f6);
		this.renderNorthFace(uu1, -0.5D, -0.5D, -0.5D, uu1.getBlockTextureFromSide(4));
		f6 = uu1.getBlockBrightness(fd, i1 + 1, j1, k1);
		if(f6 < f5) {
			f6 = f5;
		}

		nw1.setColorOpaque_F(f4 * f6, f4 * f6, f4 * f6);
		this.renderSouthFace(uu1, -0.5D, -0.5D, -0.5D, uu1.getBlockTextureFromSide(5));
		nw1.draw();
	}

	public boolean renderStandardBlock(Block uu1, int i1, int j1, int k1) {
		int l1 = uu1.colorMultiplier(this.blockAccess, i1, j1, k1);
		float f1 = (float)(l1 >> 16 & 255) / 255.0F;
		float f2 = (float)(l1 >> 8 & 255) / 255.0F;
		float f3 = (float)(l1 & 255) / 255.0F;
		if(EntityRenderer.field_28135_a) {
			float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}

		return Minecraft.isAmbientOcclusionEnabled() ? this.renderStandardBlockWithAmbientOcclusion(uu1, i1, j1, k1, f1, f2, f3) : this.renderStandardBlockWithColorMultiplier(uu1, i1, j1, k1, f1, f2, f3);
	}

	public boolean renderStandardBlockWithAmbientOcclusion(Block uu1, int i1, int j1, int k1, float f1, float f2, float f3) {
		this.enableAO = true;
		boolean flag = false;
		float f4 = this.lightValueOwn;
		float f11 = this.lightValueOwn;
		float f18 = this.lightValueOwn;
		float f25 = this.lightValueOwn;
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		boolean flag4 = true;
		boolean flag5 = true;
		boolean flag6 = true;
		this.lightValueOwn = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		this.aoLightValueXNeg = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
		this.aoLightValueYNeg = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
		this.aoLightValueZNeg = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
		this.aoLightValueXPos = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
		this.aoLightValueYPos = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
		this.aoLightValueZPos = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
		this.field_22338_U = Block.canBlockGrass[this.blockAccess.getBlockId(i1 + 1, j1 + 1, k1)];
		this.field_22359_ac = Block.canBlockGrass[this.blockAccess.getBlockId(i1 + 1, j1 - 1, k1)];
		this.field_22334_Y = Block.canBlockGrass[this.blockAccess.getBlockId(i1 + 1, j1, k1 + 1)];
		this.field_22363_aa = Block.canBlockGrass[this.blockAccess.getBlockId(i1 + 1, j1, k1 - 1)];
		this.field_22337_V = Block.canBlockGrass[this.blockAccess.getBlockId(i1 - 1, j1 + 1, k1)];
		this.field_22357_ad = Block.canBlockGrass[this.blockAccess.getBlockId(i1 - 1, j1 - 1, k1)];
		this.field_22335_X = Block.canBlockGrass[this.blockAccess.getBlockId(i1 - 1, j1, k1 - 1)];
		this.field_22333_Z = Block.canBlockGrass[this.blockAccess.getBlockId(i1 - 1, j1, k1 + 1)];
		this.field_22336_W = Block.canBlockGrass[this.blockAccess.getBlockId(i1, j1 + 1, k1 + 1)];
		this.field_22339_T = Block.canBlockGrass[this.blockAccess.getBlockId(i1, j1 + 1, k1 - 1)];
		this.field_22355_ae = Block.canBlockGrass[this.blockAccess.getBlockId(i1, j1 - 1, k1 + 1)];
		this.field_22361_ab = Block.canBlockGrass[this.blockAccess.getBlockId(i1, j1 - 1, k1 - 1)];
		if(uu1.blockIndexInTexture == 3) {
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

		float f10;
		float f17;
		float f24;
		float f31;
		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1 - 1, k1, 0)) {
			if(this.field_22352_G <= 0) {
				f31 = this.aoLightValueYNeg;
				f24 = this.aoLightValueYNeg;
				f17 = this.aoLightValueYNeg;
				f10 = this.aoLightValueYNeg;
			} else {
				--j1;
				this.field_22376_n = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
				this.field_22374_p = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
				this.field_22373_q = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
				this.field_22371_s = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
				if(!this.field_22361_ab && !this.field_22357_ad) {
					this.field_22377_m = this.field_22376_n;
				} else {
					this.field_22377_m = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1 - 1);
				}

				if(!this.field_22355_ae && !this.field_22357_ad) {
					this.field_22375_o = this.field_22376_n;
				} else {
					this.field_22375_o = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1 + 1);
				}

				if(!this.field_22361_ab && !this.field_22359_ac) {
					this.field_22372_r = this.field_22371_s;
				} else {
					this.field_22372_r = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1 - 1);
				}

				if(!this.field_22355_ae && !this.field_22359_ac) {
					this.field_22370_t = this.field_22371_s;
				} else {
					this.field_22370_t = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1 + 1);
				}

				++j1;
				f10 = (this.field_22375_o + this.field_22376_n + this.field_22373_q + this.aoLightValueYNeg) / 4.0F;
				f31 = (this.field_22373_q + this.aoLightValueYNeg + this.field_22370_t + this.field_22371_s) / 4.0F;
				f24 = (this.aoLightValueYNeg + this.field_22374_p + this.field_22371_s + this.field_22372_r) / 4.0F;
				f17 = (this.field_22376_n + this.field_22377_m + this.aoLightValueYNeg + this.field_22374_p) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag1 ? f1 : 1.0F) * 0.5F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag1 ? f2 : 1.0F) * 0.5F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag1 ? f3 : 1.0F) * 0.5F;
			this.colorRedTopLeft *= f10;
			this.colorGreenTopLeft *= f10;
			this.colorBlueTopLeft *= f10;
			this.colorRedBottomLeft *= f17;
			this.colorGreenBottomLeft *= f17;
			this.colorBlueBottomLeft *= f17;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f31;
			this.colorGreenTopRight *= f31;
			this.colorBlueTopRight *= f31;
			this.renderBottomFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 0));
			flag = true;
		}

		int k2;
		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1 + 1, k1, 1)) {
			if(this.field_22352_G <= 0) {
				f31 = this.aoLightValueYPos;
				f24 = this.aoLightValueYPos;
				f17 = this.aoLightValueYPos;
				f10 = this.aoLightValueYPos;
			} else {
				++j1;
				this.field_22368_v = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
				this.field_22364_z = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
				this.field_22366_x = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
				this.field_22362_A = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
				if(!this.field_22339_T && !this.field_22337_V) {
					this.field_22369_u = this.field_22368_v;
				} else {
					this.field_22369_u = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1 - 1);
				}

				if(!this.field_22339_T && !this.field_22338_U) {
					this.field_22365_y = this.field_22364_z;
				} else {
					this.field_22365_y = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1 - 1);
				}

				if(!this.field_22336_W && !this.field_22337_V) {
					this.field_22367_w = this.field_22368_v;
				} else {
					this.field_22367_w = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1 + 1);
				}

				if(!this.field_22336_W && !this.field_22338_U) {
					this.field_22360_B = this.field_22364_z;
				} else {
					this.field_22360_B = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1 + 1);
				}

				--j1;
				f31 = (this.field_22367_w + this.field_22368_v + this.field_22362_A + this.aoLightValueYPos) / 4.0F;
				f10 = (this.field_22362_A + this.aoLightValueYPos + this.field_22360_B + this.field_22364_z) / 4.0F;
				f17 = (this.aoLightValueYPos + this.field_22366_x + this.field_22364_z + this.field_22365_y) / 4.0F;
				f24 = (this.field_22368_v + this.field_22369_u + this.aoLightValueYPos + this.field_22366_x) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = flag2 ? f1 : 1.0F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = flag2 ? f2 : 1.0F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = flag2 ? f3 : 1.0F;
			this.colorRedTopLeft *= f10;
			this.colorGreenTopLeft *= f10;
			this.colorBlueTopLeft *= f10;
			this.colorRedBottomLeft *= f17;
			this.colorGreenBottomLeft *= f17;
			this.colorBlueBottomLeft *= f17;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f31;
			this.colorGreenTopRight *= f31;
			this.colorBlueTopRight *= f31;
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 1);
			this.renderTopFace(uu1, (double)i1, (double)j1, (double)k1, k2);
			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 - 1, 2)) {
			if(this.field_22352_G <= 0) {
				f31 = this.aoLightValueZNeg;
				f24 = this.aoLightValueZNeg;
				f17 = this.aoLightValueZNeg;
				f10 = this.aoLightValueZNeg;
			} else {
				--k1;
				this.field_22358_C = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
				this.field_22374_p = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
				this.field_22366_x = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
				this.field_22356_D = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
				if(!this.field_22335_X && !this.field_22361_ab) {
					this.field_22377_m = this.field_22358_C;
				} else {
					this.field_22377_m = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1 - 1, k1);
				}

				if(!this.field_22335_X && !this.field_22339_T) {
					this.field_22369_u = this.field_22358_C;
				} else {
					this.field_22369_u = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1 + 1, k1);
				}

				if(!this.field_22363_aa && !this.field_22361_ab) {
					this.field_22372_r = this.field_22356_D;
				} else {
					this.field_22372_r = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1 - 1, k1);
				}

				if(!this.field_22363_aa && !this.field_22339_T) {
					this.field_22365_y = this.field_22356_D;
				} else {
					this.field_22365_y = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1 + 1, k1);
				}

				++k1;
				f10 = (this.field_22358_C + this.field_22369_u + this.aoLightValueZNeg + this.field_22366_x) / 4.0F;
				f17 = (this.aoLightValueZNeg + this.field_22366_x + this.field_22356_D + this.field_22365_y) / 4.0F;
				f24 = (this.field_22374_p + this.aoLightValueZNeg + this.field_22372_r + this.field_22356_D) / 4.0F;
				f31 = (this.field_22377_m + this.field_22358_C + this.field_22374_p + this.aoLightValueZNeg) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag3 ? f1 : 1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag3 ? f2 : 1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag3 ? f3 : 1.0F) * 0.8F;
			this.colorRedTopLeft *= f10;
			this.colorGreenTopLeft *= f10;
			this.colorBlueTopLeft *= f10;
			this.colorRedBottomLeft *= f17;
			this.colorGreenBottomLeft *= f17;
			this.colorBlueBottomLeft *= f17;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f31;
			this.colorGreenTopRight *= f31;
			this.colorBlueTopRight *= f31;
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 2);
			this.renderEastFace(uu1, (double)i1, (double)j1, (double)k1, k2);
			if(cfgGrassFix && k2 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f1;
				this.colorRedBottomLeft *= f1;
				this.colorRedBottomRight *= f1;
				this.colorRedTopRight *= f1;
				this.colorGreenTopLeft *= f2;
				this.colorGreenBottomLeft *= f2;
				this.colorGreenBottomRight *= f2;
				this.colorGreenTopRight *= f2;
				this.colorBlueTopLeft *= f3;
				this.colorBlueBottomLeft *= f3;
				this.colorBlueBottomRight *= f3;
				this.colorBlueTopRight *= f3;
				this.renderEastFace(uu1, (double)i1, (double)j1, (double)k1, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 + 1, 3)) {
			if(this.field_22352_G <= 0) {
				f31 = this.aoLightValueZPos;
				f24 = this.aoLightValueZPos;
				f17 = this.aoLightValueZPos;
				f10 = this.aoLightValueZPos;
			} else {
				++k1;
				this.field_22354_E = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
				this.field_22353_F = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
				this.field_22373_q = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
				this.field_22362_A = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
				if(!this.field_22333_Z && !this.field_22355_ae) {
					this.field_22375_o = this.field_22354_E;
				} else {
					this.field_22375_o = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1 - 1, k1);
				}

				if(!this.field_22333_Z && !this.field_22336_W) {
					this.field_22367_w = this.field_22354_E;
				} else {
					this.field_22367_w = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1 + 1, k1);
				}

				if(!this.field_22334_Y && !this.field_22355_ae) {
					this.field_22370_t = this.field_22353_F;
				} else {
					this.field_22370_t = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1 - 1, k1);
				}

				if(!this.field_22334_Y && !this.field_22336_W) {
					this.field_22360_B = this.field_22353_F;
				} else {
					this.field_22360_B = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1 + 1, k1);
				}

				--k1;
				f10 = (this.field_22354_E + this.field_22367_w + this.aoLightValueZPos + this.field_22362_A) / 4.0F;
				f31 = (this.aoLightValueZPos + this.field_22362_A + this.field_22353_F + this.field_22360_B) / 4.0F;
				f24 = (this.field_22373_q + this.aoLightValueZPos + this.field_22370_t + this.field_22353_F) / 4.0F;
				f17 = (this.field_22375_o + this.field_22354_E + this.field_22373_q + this.aoLightValueZPos) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag4 ? f1 : 1.0F) * 0.8F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag4 ? f2 : 1.0F) * 0.8F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag4 ? f3 : 1.0F) * 0.8F;
			this.colorRedTopLeft *= f10;
			this.colorGreenTopLeft *= f10;
			this.colorBlueTopLeft *= f10;
			this.colorRedBottomLeft *= f17;
			this.colorGreenBottomLeft *= f17;
			this.colorBlueBottomLeft *= f17;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f31;
			this.colorGreenTopRight *= f31;
			this.colorBlueTopRight *= f31;
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 3);
			this.renderWestFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 3));
			if(cfgGrassFix && k2 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f1;
				this.colorRedBottomLeft *= f1;
				this.colorRedBottomRight *= f1;
				this.colorRedTopRight *= f1;
				this.colorGreenTopLeft *= f2;
				this.colorGreenBottomLeft *= f2;
				this.colorGreenBottomRight *= f2;
				this.colorGreenTopRight *= f2;
				this.colorBlueTopLeft *= f3;
				this.colorBlueBottomLeft *= f3;
				this.colorBlueBottomRight *= f3;
				this.colorBlueTopRight *= f3;
				this.renderWestFace(uu1, (double)i1, (double)j1, (double)k1, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1 - 1, j1, k1, 4)) {
			if(this.field_22352_G <= 0) {
				f31 = this.aoLightValueXNeg;
				f24 = this.aoLightValueXNeg;
				f17 = this.aoLightValueXNeg;
				f10 = this.aoLightValueXNeg;
			} else {
				--i1;
				this.field_22376_n = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
				this.field_22358_C = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
				this.field_22354_E = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
				this.field_22368_v = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
				if(!this.field_22335_X && !this.field_22357_ad) {
					this.field_22377_m = this.field_22358_C;
				} else {
					this.field_22377_m = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1 - 1);
				}

				if(!this.field_22333_Z && !this.field_22357_ad) {
					this.field_22375_o = this.field_22354_E;
				} else {
					this.field_22375_o = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1 + 1);
				}

				if(!this.field_22335_X && !this.field_22337_V) {
					this.field_22369_u = this.field_22358_C;
				} else {
					this.field_22369_u = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1 - 1);
				}

				if(!this.field_22333_Z && !this.field_22337_V) {
					this.field_22367_w = this.field_22354_E;
				} else {
					this.field_22367_w = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1 + 1);
				}

				++i1;
				f31 = (this.field_22376_n + this.field_22375_o + this.aoLightValueXNeg + this.field_22354_E) / 4.0F;
				f10 = (this.aoLightValueXNeg + this.field_22354_E + this.field_22368_v + this.field_22367_w) / 4.0F;
				f17 = (this.field_22358_C + this.aoLightValueXNeg + this.field_22369_u + this.field_22368_v) / 4.0F;
				f24 = (this.field_22377_m + this.field_22376_n + this.field_22358_C + this.aoLightValueXNeg) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag5 ? f1 : 1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag5 ? f2 : 1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag5 ? f3 : 1.0F) * 0.6F;
			this.colorRedTopLeft *= f10;
			this.colorGreenTopLeft *= f10;
			this.colorBlueTopLeft *= f10;
			this.colorRedBottomLeft *= f17;
			this.colorGreenBottomLeft *= f17;
			this.colorBlueBottomLeft *= f17;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f31;
			this.colorGreenTopRight *= f31;
			this.colorBlueTopRight *= f31;
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 4);
			this.renderNorthFace(uu1, (double)i1, (double)j1, (double)k1, k2);
			if(cfgGrassFix && k2 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f1;
				this.colorRedBottomLeft *= f1;
				this.colorRedBottomRight *= f1;
				this.colorRedTopRight *= f1;
				this.colorGreenTopLeft *= f2;
				this.colorGreenBottomLeft *= f2;
				this.colorGreenBottomRight *= f2;
				this.colorGreenTopRight *= f2;
				this.colorBlueTopLeft *= f3;
				this.colorBlueBottomLeft *= f3;
				this.colorBlueBottomRight *= f3;
				this.colorBlueTopRight *= f3;
				this.renderNorthFace(uu1, (double)i1, (double)j1, (double)k1, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1 + 1, j1, k1, 5)) {
			if(this.field_22352_G <= 0) {
				f31 = this.aoLightValueXPos;
				f24 = this.aoLightValueXPos;
				f17 = this.aoLightValueXPos;
				f10 = this.aoLightValueXPos;
			} else {
				++i1;
				this.field_22371_s = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
				this.field_22356_D = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
				this.field_22353_F = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
				this.field_22364_z = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
				if(!this.field_22359_ac && !this.field_22363_aa) {
					this.field_22372_r = this.field_22356_D;
				} else {
					this.field_22372_r = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1 - 1);
				}

				if(!this.field_22359_ac && !this.field_22334_Y) {
					this.field_22370_t = this.field_22353_F;
				} else {
					this.field_22370_t = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1 + 1);
				}

				if(!this.field_22338_U && !this.field_22363_aa) {
					this.field_22365_y = this.field_22356_D;
				} else {
					this.field_22365_y = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1 - 1);
				}

				if(!this.field_22338_U && !this.field_22334_Y) {
					this.field_22360_B = this.field_22353_F;
				} else {
					this.field_22360_B = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1 + 1);
				}

				--i1;
				f10 = (this.field_22371_s + this.field_22370_t + this.aoLightValueXPos + this.field_22353_F) / 4.0F;
				f31 = (this.aoLightValueXPos + this.field_22353_F + this.field_22364_z + this.field_22360_B) / 4.0F;
				f24 = (this.field_22356_D + this.aoLightValueXPos + this.field_22365_y + this.field_22364_z) / 4.0F;
				f17 = (this.field_22372_r + this.field_22371_s + this.field_22356_D + this.aoLightValueXPos) / 4.0F;
			}

			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag6 ? f1 : 1.0F) * 0.6F;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag6 ? f2 : 1.0F) * 0.6F;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag6 ? f3 : 1.0F) * 0.6F;
			this.colorRedTopLeft *= f10;
			this.colorGreenTopLeft *= f10;
			this.colorBlueTopLeft *= f10;
			this.colorRedBottomLeft *= f17;
			this.colorGreenBottomLeft *= f17;
			this.colorBlueBottomLeft *= f17;
			this.colorRedBottomRight *= f24;
			this.colorGreenBottomRight *= f24;
			this.colorBlueBottomRight *= f24;
			this.colorRedTopRight *= f31;
			this.colorGreenTopRight *= f31;
			this.colorBlueTopRight *= f31;
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 5);
			this.renderSouthFace(uu1, (double)i1, (double)j1, (double)k1, k2);
			if(cfgGrassFix && k2 == 3 && this.overrideBlockTexture < 0) {
				this.colorRedTopLeft *= f1;
				this.colorRedBottomLeft *= f1;
				this.colorRedBottomRight *= f1;
				this.colorRedTopRight *= f1;
				this.colorGreenTopLeft *= f2;
				this.colorGreenBottomLeft *= f2;
				this.colorGreenBottomRight *= f2;
				this.colorGreenTopRight *= f2;
				this.colorBlueTopLeft *= f3;
				this.colorBlueBottomLeft *= f3;
				this.colorBlueBottomRight *= f3;
				this.colorBlueTopRight *= f3;
				this.renderSouthFace(uu1, (double)i1, (double)j1, (double)k1, 38);
			}

			flag = true;
		}

		this.enableAO = false;
		return flag;
	}

	public boolean renderStandardBlockWithColorMultiplier(Block uu1, int i1, int j1, int k1, float f1, float f2, float f3) {
		this.enableAO = false;
		Tessellator nw1 = Tessellator.instance;
		boolean flag = false;
		float f4 = 0.5F;
		float f5 = 1.0F;
		float f6 = 0.8F;
		float f7 = 0.6F;
		float f8 = f5 * f1;
		float f9 = f5 * f2;
		float f10 = f5 * f3;
		float f11 = f4;
		float f12 = f6;
		float f13 = f7;
		float f14 = f4;
		float f15 = f6;
		float f16 = f7;
		float f17 = f4;
		float f18 = f6;
		float f19 = f7;
		if(uu1 != Block.grass) {
			f11 = f4 * f1;
			f12 = f6 * f1;
			f13 = f7 * f1;
			f14 = f4 * f2;
			f15 = f6 * f2;
			f16 = f7 * f2;
			f17 = f4 * f3;
			f18 = f6 * f3;
			f19 = f7 * f3;
		}

		float f20 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		float f26;
		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1 - 1, k1, 0)) {
			f26 = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
			nw1.setColorOpaque_F(f11 * f26, f14 * f26, f17 * f26);
			this.renderBottomFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 0));
			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1 + 1, k1, 1)) {
			f26 = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
			if(uu1.maxY != 1.0D && !uu1.blockMaterial.getIsLiquid()) {
				f26 = f20;
			}

			nw1.setColorOpaque_F(f8 * f26, f9 * f26, f10 * f26);
			this.renderTopFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 1));
			flag = true;
		}

		int k2;
		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 - 1, 2)) {
			f26 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
			if(uu1.minZ > 0.0D) {
				f26 = f20;
			}

			nw1.setColorOpaque_F(f12 * f26, f15 * f26, f18 * f26);
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 2);
			this.renderEastFace(uu1, (double)i1, (double)j1, (double)k1, k2);
			if(cfgGrassFix && k2 == 3 && this.overrideBlockTexture < 0) {
				nw1.setColorOpaque_F(f12 * f26 * f1, f15 * f26 * f2, f18 * f26 * f3);
				this.renderEastFace(uu1, (double)i1, (double)j1, (double)k1, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 + 1, 3)) {
			f26 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
			if(uu1.maxZ < 1.0D) {
				f26 = f20;
			}

			nw1.setColorOpaque_F(f12 * f26, f15 * f26, f18 * f26);
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 3);
			this.renderWestFace(uu1, (double)i1, (double)j1, (double)k1, k2);
			if(cfgGrassFix && k2 == 3 && this.overrideBlockTexture < 0) {
				nw1.setColorOpaque_F(f12 * f26 * f1, f15 * f26 * f2, f18 * f26 * f3);
				this.renderWestFace(uu1, (double)i1, (double)j1, (double)k1, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1 - 1, j1, k1, 4)) {
			f26 = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
			if(uu1.minX > 0.0D) {
				f26 = f20;
			}

			nw1.setColorOpaque_F(f13 * f26, f16 * f26, f19 * f26);
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 4);
			this.renderNorthFace(uu1, (double)i1, (double)j1, (double)k1, k2);
			if(cfgGrassFix && k2 == 3 && this.overrideBlockTexture < 0) {
				nw1.setColorOpaque_F(f13 * f26 * f1, f16 * f26 * f2, f19 * f26 * f3);
				this.renderNorthFace(uu1, (double)i1, (double)j1, (double)k1, 38);
			}

			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1 + 1, j1, k1, 5)) {
			f26 = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
			if(uu1.maxX < 1.0D) {
				f26 = f20;
			}

			nw1.setColorOpaque_F(f13 * f26, f16 * f26, f19 * f26);
			k2 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 5);
			this.renderSouthFace(uu1, (double)i1, (double)j1, (double)k1, k2);
			if(cfgGrassFix && k2 == 3 && this.overrideBlockTexture < 0) {
				nw1.setColorOpaque_F(f13 * f26 * f1, f16 * f26 * f2, f19 * f26 * f3);
				this.renderSouthFace(uu1, (double)i1, (double)j1, (double)k1, 38);
			}

			flag = true;
		}

		return flag;
	}

	public boolean renderBlockCactus(Block uu1, int i1, int j1, int k1) {
		int l1 = uu1.colorMultiplier(this.blockAccess, i1, j1, k1);
		float f1 = (float)(l1 >> 16 & 255) / 255.0F;
		float f2 = (float)(l1 >> 8 & 255) / 255.0F;
		float f3 = (float)(l1 & 255) / 255.0F;
		if(EntityRenderer.field_28135_a) {
			float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}

		return this.func_1230_b(uu1, i1, j1, k1, f1, f2, f3);
	}

	public boolean func_1230_b(Block uu1, int i1, int j1, int k1, float f1, float f2, float f3) {
		Tessellator nw1 = Tessellator.instance;
		boolean flag = false;
		float f4 = 0.5F;
		float f5 = 1.0F;
		float f6 = 0.8F;
		float f7 = 0.6F;
		float f8 = f4 * f1;
		float f9 = f5 * f1;
		float f10 = f6 * f1;
		float f11 = f7 * f1;
		float f12 = f4 * f2;
		float f13 = f5 * f2;
		float f14 = f6 * f2;
		float f15 = f7 * f2;
		float f16 = f4 * f3;
		float f17 = f5 * f3;
		float f18 = f6 * f3;
		float f19 = f7 * f3;
		float f20 = 0.0625F;
		float f21 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		float f27;
		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1 - 1, k1, 0)) {
			f27 = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
			nw1.setColorOpaque_F(f8 * f27, f12 * f27, f16 * f27);
			this.renderBottomFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 0));
			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1 + 1, k1, 1)) {
			f27 = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
			if(uu1.maxY != 1.0D && !uu1.blockMaterial.getIsLiquid()) {
				f27 = f21;
			}

			nw1.setColorOpaque_F(f9 * f27, f13 * f27, f17 * f27);
			this.renderTopFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 1));
			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 - 1, 2)) {
			f27 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
			if(uu1.minZ > 0.0D) {
				f27 = f21;
			}

			nw1.setColorOpaque_F(f10 * f27, f14 * f27, f18 * f27);
			nw1.setTranslationF(0.0F, 0.0F, f20);
			this.renderEastFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 2));
			nw1.setTranslationF(0.0F, 0.0F, -f20);
			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1, j1, k1 + 1, 3)) {
			f27 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
			if(uu1.maxZ < 1.0D) {
				f27 = f21;
			}

			nw1.setColorOpaque_F(f10 * f27, f14 * f27, f18 * f27);
			nw1.setTranslationF(0.0F, 0.0F, -f20);
			this.renderWestFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 3));
			nw1.setTranslationF(0.0F, 0.0F, f20);
			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1 - 1, j1, k1, 4)) {
			f27 = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
			if(uu1.minX > 0.0D) {
				f27 = f21;
			}

			nw1.setColorOpaque_F(f11 * f27, f15 * f27, f19 * f27);
			nw1.setTranslationF(f20, 0.0F, 0.0F);
			this.renderNorthFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 4));
			nw1.setTranslationF(-f20, 0.0F, 0.0F);
			flag = true;
		}

		if(this.renderAllFaces || uu1.shouldSideBeRendered(this.blockAccess, i1 + 1, j1, k1, 5)) {
			f27 = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
			if(uu1.maxX < 1.0D) {
				f27 = f21;
			}

			nw1.setColorOpaque_F(f11 * f27, f15 * f27, f19 * f27);
			nw1.setTranslationF(-f20, 0.0F, 0.0F);
			this.renderSouthFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 5));
			nw1.setTranslationF(f20, 0.0F, 0.0F);
			flag = true;
		}

		return flag;
	}

	public boolean renderBlockFence(Block uu1, int i1, int j1, int k1) {
		boolean flag = false;
		float f1 = 0.375F;
		float f2 = 0.625F;
		uu1.setBlockBounds(f1, 0.0F, f1, f2, 1.0F, f2);
		this.renderStandardBlock(uu1, i1, j1, k1);
		flag = true;
		boolean flag1 = false;
		boolean flag2 = false;
		if(this.blockAccess.getBlockId(i1 - 1, j1, k1) == uu1.blockID || this.blockAccess.getBlockId(i1 + 1, j1, k1) == uu1.blockID) {
			flag1 = true;
		}

		if(this.blockAccess.getBlockId(i1, j1, k1 - 1) == uu1.blockID || this.blockAccess.getBlockId(i1, j1, k1 + 1) == uu1.blockID) {
			flag2 = true;
		}

		boolean flag3 = this.blockAccess.getBlockId(i1 - 1, j1, k1) == uu1.blockID;
		boolean flag4 = this.blockAccess.getBlockId(i1 + 1, j1, k1) == uu1.blockID;
		boolean flag5 = this.blockAccess.getBlockId(i1, j1, k1 - 1) == uu1.blockID;
		boolean flag6 = this.blockAccess.getBlockId(i1, j1, k1 + 1) == uu1.blockID;
		if(!flag1 && !flag2) {
			flag1 = true;
		}

		f1 = 0.4375F;
		f2 = 0.5625F;
		float f3 = 0.75F;
		float f4 = 0.9375F;
		float f5 = flag3 ? 0.0F : f1;
		float f6 = flag4 ? 1.0F : f2;
		float f7 = flag5 ? 0.0F : f1;
		float f8 = flag6 ? 1.0F : f2;
		if(flag1) {
			uu1.setBlockBounds(f5, f3, f1, f6, f4, f2);
			this.renderStandardBlock(uu1, i1, j1, k1);
			flag = true;
		}

		if(flag2) {
			uu1.setBlockBounds(f1, f3, f7, f2, f4, f8);
			this.renderStandardBlock(uu1, i1, j1, k1);
			flag = true;
		}

		f3 = 0.375F;
		f4 = 0.5625F;
		if(flag1) {
			uu1.setBlockBounds(f5, f3, f1, f6, f4, f2);
			this.renderStandardBlock(uu1, i1, j1, k1);
			flag = true;
		}

		if(flag2) {
			uu1.setBlockBounds(f1, f3, f7, f2, f4, f8);
			this.renderStandardBlock(uu1, i1, j1, k1);
			flag = true;
		}

		uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return flag;
	}

	public boolean renderBlockStairs(Block uu1, int i1, int j1, int k1) {
		boolean flag = false;
		int l1 = this.blockAccess.getBlockMetadata(i1, j1, k1);
		if(l1 == 0) {
			uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			uu1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			flag = true;
		} else if(l1 == 1) {
			uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			uu1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			flag = true;
		} else if(l1 == 2) {
			uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			uu1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			flag = true;
		} else if(l1 == 3) {
			uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			uu1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			this.renderStandardBlock(uu1, i1, j1, k1);
			flag = true;
		}

		uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return flag;
	}

	public boolean renderBlockDoor(Block uu1, int i1, int j1, int k1) {
		Tessellator nw1 = Tessellator.instance;
		BlockDoor le1 = (BlockDoor)uu1;
		boolean flag = false;
		float f1 = 0.5F;
		float f2 = 1.0F;
		float f3 = 0.8F;
		float f4 = 0.6F;
		float f5 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1);
		float f6 = uu1.getBlockBrightness(this.blockAccess, i1, j1 - 1, k1);
		if(le1.minY > 0.0D) {
			f6 = f5;
		}

		if(Block.lightValue[uu1.blockID] > 0) {
			f6 = 1.0F;
		}

		nw1.setColorOpaque_F(f1 * f6, f1 * f6, f1 * f6);
		this.renderBottomFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 0));
		flag = true;
		f6 = uu1.getBlockBrightness(this.blockAccess, i1, j1 + 1, k1);
		if(le1.maxY < 1.0D) {
			f6 = f5;
		}

		if(Block.lightValue[uu1.blockID] > 0) {
			f6 = 1.0F;
		}

		nw1.setColorOpaque_F(f2 * f6, f2 * f6, f2 * f6);
		this.renderTopFace(uu1, (double)i1, (double)j1, (double)k1, uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 1));
		flag = true;
		f6 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 - 1);
		if(le1.minZ > 0.0D) {
			f6 = f5;
		}

		if(Block.lightValue[uu1.blockID] > 0) {
			f6 = 1.0F;
		}

		nw1.setColorOpaque_F(f3 * f6, f3 * f6, f3 * f6);
		int l1 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 2);
		if(l1 < 0) {
			this.flipTexture = true;
			l1 = -l1;
		}

		this.renderEastFace(uu1, (double)i1, (double)j1, (double)k1, l1);
		flag = true;
		this.flipTexture = false;
		f6 = uu1.getBlockBrightness(this.blockAccess, i1, j1, k1 + 1);
		if(le1.maxZ < 1.0D) {
			f6 = f5;
		}

		if(Block.lightValue[uu1.blockID] > 0) {
			f6 = 1.0F;
		}

		nw1.setColorOpaque_F(f3 * f6, f3 * f6, f3 * f6);
		l1 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 3);
		if(l1 < 0) {
			this.flipTexture = true;
			l1 = -l1;
		}

		this.renderWestFace(uu1, (double)i1, (double)j1, (double)k1, l1);
		flag = true;
		this.flipTexture = false;
		f6 = uu1.getBlockBrightness(this.blockAccess, i1 - 1, j1, k1);
		if(le1.minX > 0.0D) {
			f6 = f5;
		}

		if(Block.lightValue[uu1.blockID] > 0) {
			f6 = 1.0F;
		}

		nw1.setColorOpaque_F(f4 * f6, f4 * f6, f4 * f6);
		l1 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 4);
		if(l1 < 0) {
			this.flipTexture = true;
			l1 = -l1;
		}

		this.renderNorthFace(uu1, (double)i1, (double)j1, (double)k1, l1);
		flag = true;
		this.flipTexture = false;
		f6 = uu1.getBlockBrightness(this.blockAccess, i1 + 1, j1, k1);
		if(le1.maxX < 1.0D) {
			f6 = f5;
		}

		if(Block.lightValue[uu1.blockID] > 0) {
			f6 = 1.0F;
		}

		nw1.setColorOpaque_F(f4 * f6, f4 * f6, f4 * f6);
		l1 = uu1.getBlockTexture(this.blockAccess, i1, j1, k1, 5);
		if(l1 < 0) {
			this.flipTexture = true;
			l1 = -l1;
		}

		this.renderSouthFace(uu1, (double)i1, (double)j1, (double)k1, l1);
		flag = true;
		this.flipTexture = false;
		return flag;
	}

	public void renderBottomFace(Block uu1, double d1, double d2, double d3, int i1) {
		Tessellator nw1 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		double d4 = ((double)j1 + uu1.minX * 16.0D) / 256.0D;
		double d5 = ((double)j1 + uu1.maxX * 16.0D - 0.01D) / 256.0D;
		double d6 = ((double)k1 + uu1.minZ * 16.0D) / 256.0D;
		double d7 = ((double)k1 + uu1.maxZ * 16.0D - 0.01D) / 256.0D;
		if(uu1.minX < 0.0D || uu1.maxX > 1.0D) {
			d4 = (double)(((float)j1 + 0.0F) / 256.0F);
			d5 = (double)(((float)j1 + 15.99F) / 256.0F);
		}

		if(uu1.minZ < 0.0D || uu1.maxZ > 1.0D) {
			d6 = (double)(((float)k1 + 0.0F) / 256.0F);
			d7 = (double)(((float)k1 + 15.99F) / 256.0F);
		}

		double d8 = d5;
		double d9 = d4;
		double d10 = d6;
		double d11 = d7;
		if(this.field_31082_l == 2) {
			d4 = ((double)j1 + uu1.minZ * 16.0D) / 256.0D;
			d6 = ((double)(k1 + 16) - uu1.maxX * 16.0D) / 256.0D;
			d5 = ((double)j1 + uu1.maxZ * 16.0D) / 256.0D;
			d7 = ((double)(k1 + 16) - uu1.minX * 16.0D) / 256.0D;
			d10 = d6;
			d11 = d7;
			d8 = d4;
			d9 = d5;
			d6 = d7;
			d7 = d10;
		} else if(this.field_31082_l == 1) {
			d4 = ((double)(j1 + 16) - uu1.maxZ * 16.0D) / 256.0D;
			d6 = ((double)k1 + uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.minZ * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.maxX * 16.0D) / 256.0D;
			d8 = d5;
			d9 = d4;
			d4 = d5;
			d5 = d9;
			d10 = d7;
			d11 = d6;
		} else if(this.field_31082_l == 3) {
			d4 = ((double)(j1 + 16) - uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.maxX * 16.0D - 0.01D) / 256.0D;
			d6 = ((double)(k1 + 16) - uu1.minZ * 16.0D) / 256.0D;
			d7 = ((double)(k1 + 16) - uu1.maxZ * 16.0D - 0.01D) / 256.0D;
			d8 = d5;
			d9 = d4;
			d10 = d6;
			d11 = d7;
		}

		double d12 = d1 + uu1.minX;
		double d13 = d1 + uu1.maxX;
		double d14 = d2 + uu1.minY;
		double d15 = d3 + uu1.minZ;
		double d16 = d3 + uu1.maxZ;
		if(this.enableAO) {
			nw1.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			nw1.addVertexWithUV(d12, d14, d16, d9, d11);
			nw1.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			nw1.addVertexWithUV(d12, d14, d15, d4, d6);
			nw1.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			nw1.addVertexWithUV(d13, d14, d15, d8, d10);
			nw1.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			nw1.addVertexWithUV(d13, d14, d16, d5, d7);
		} else {
			nw1.addVertexWithUV(d12, d14, d16, d9, d11);
			nw1.addVertexWithUV(d12, d14, d15, d4, d6);
			nw1.addVertexWithUV(d13, d14, d15, d8, d10);
			nw1.addVertexWithUV(d13, d14, d16, d5, d7);
		}

	}

	public void renderTopFace(Block uu1, double d1, double d2, double d3, int i1) {
		Tessellator nw1 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		double d4 = ((double)j1 + uu1.minX * 16.0D) / 256.0D;
		double d5 = ((double)j1 + uu1.maxX * 16.0D - 0.01D) / 256.0D;
		double d6 = ((double)k1 + uu1.minZ * 16.0D) / 256.0D;
		double d7 = ((double)k1 + uu1.maxZ * 16.0D - 0.01D) / 256.0D;
		if(uu1.minX < 0.0D || uu1.maxX > 1.0D) {
			d4 = (double)(((float)j1 + 0.0F) / 256.0F);
			d5 = (double)(((float)j1 + 15.99F) / 256.0F);
		}

		if(uu1.minZ < 0.0D || uu1.maxZ > 1.0D) {
			d6 = (double)(((float)k1 + 0.0F) / 256.0F);
			d7 = (double)(((float)k1 + 15.99F) / 256.0F);
		}

		double d8 = d5;
		double d9 = d4;
		double d10 = d6;
		double d11 = d7;
		if(this.field_31083_k == 1) {
			d4 = ((double)j1 + uu1.minZ * 16.0D) / 256.0D;
			d6 = ((double)(k1 + 16) - uu1.maxX * 16.0D) / 256.0D;
			d5 = ((double)j1 + uu1.maxZ * 16.0D) / 256.0D;
			d7 = ((double)(k1 + 16) - uu1.minX * 16.0D) / 256.0D;
			d10 = d6;
			d11 = d7;
			d8 = d4;
			d9 = d5;
			d6 = d7;
			d7 = d10;
		} else if(this.field_31083_k == 2) {
			d4 = ((double)(j1 + 16) - uu1.maxZ * 16.0D) / 256.0D;
			d6 = ((double)k1 + uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.minZ * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.maxX * 16.0D) / 256.0D;
			d8 = d5;
			d9 = d4;
			d4 = d5;
			d5 = d9;
			d10 = d7;
			d11 = d6;
		} else if(this.field_31083_k == 3) {
			d4 = ((double)(j1 + 16) - uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.maxX * 16.0D - 0.01D) / 256.0D;
			d6 = ((double)(k1 + 16) - uu1.minZ * 16.0D) / 256.0D;
			d7 = ((double)(k1 + 16) - uu1.maxZ * 16.0D - 0.01D) / 256.0D;
			d8 = d5;
			d9 = d4;
			d10 = d6;
			d11 = d7;
		}

		double d12 = d1 + uu1.minX;
		double d13 = d1 + uu1.maxX;
		double d14 = d2 + uu1.maxY;
		double d15 = d3 + uu1.minZ;
		double d16 = d3 + uu1.maxZ;
		if(this.enableAO) {
			nw1.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			nw1.addVertexWithUV(d13, d14, d16, d5, d7);
			nw1.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			nw1.addVertexWithUV(d13, d14, d15, d8, d10);
			nw1.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			nw1.addVertexWithUV(d12, d14, d15, d4, d6);
			nw1.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			nw1.addVertexWithUV(d12, d14, d16, d9, d11);
		} else {
			nw1.addVertexWithUV(d13, d14, d16, d5, d7);
			nw1.addVertexWithUV(d13, d14, d15, d8, d10);
			nw1.addVertexWithUV(d12, d14, d15, d4, d6);
			nw1.addVertexWithUV(d12, d14, d16, d9, d11);
		}

	}

	public void renderEastFace(Block uu1, double d1, double d2, double d3, int i1) {
		Tessellator nw1 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		double d4 = ((double)j1 + uu1.minX * 16.0D) / 256.0D;
		double d5 = ((double)j1 + uu1.maxX * 16.0D - 0.01D) / 256.0D;
		double d6 = ((double)(k1 + 16) - uu1.maxY * 16.0D) / 256.0D;
		double d7 = ((double)(k1 + 16) - uu1.minY * 16.0D - 0.01D) / 256.0D;
		double d9;
		if(this.flipTexture) {
			d9 = d4;
			d4 = d5;
			d5 = d9;
		}

		if(uu1.minX < 0.0D || uu1.maxX > 1.0D) {
			d4 = (double)(((float)j1 + 0.0F) / 256.0F);
			d5 = (double)(((float)j1 + 15.99F) / 256.0F);
		}

		if(uu1.minY < 0.0D || uu1.maxY > 1.0D) {
			d6 = (double)(((float)k1 + 0.0F) / 256.0F);
			d7 = (double)(((float)k1 + 15.99F) / 256.0F);
		}

		d9 = d5;
		double d10 = d4;
		double d11 = d6;
		double d12 = d7;
		if(this.field_31087_g == 2) {
			d4 = ((double)j1 + uu1.minY * 16.0D) / 256.0D;
			d6 = ((double)(k1 + 16) - uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)j1 + uu1.maxY * 16.0D) / 256.0D;
			d7 = ((double)(k1 + 16) - uu1.maxX * 16.0D) / 256.0D;
			d11 = d6;
			d12 = d7;
			d9 = d4;
			d10 = d5;
			d6 = d7;
			d7 = d11;
		} else if(this.field_31087_g == 1) {
			d4 = ((double)(j1 + 16) - uu1.maxY * 16.0D) / 256.0D;
			d6 = ((double)k1 + uu1.maxX * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.minY * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.minX * 16.0D) / 256.0D;
			d9 = d5;
			d10 = d4;
			d4 = d5;
			d5 = d10;
			d11 = d7;
			d12 = d6;
		} else if(this.field_31087_g == 3) {
			d4 = ((double)(j1 + 16) - uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.maxX * 16.0D - 0.01D) / 256.0D;
			d6 = ((double)k1 + uu1.maxY * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.minY * 16.0D - 0.01D) / 256.0D;
			d9 = d5;
			d10 = d4;
			d11 = d6;
			d12 = d7;
		}

		double d13 = d1 + uu1.minX;
		double d14 = d1 + uu1.maxX;
		double d15 = d2 + uu1.minY;
		double d16 = d2 + uu1.maxY;
		double d17 = d3 + uu1.minZ;
		if(this.enableAO) {
			nw1.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			nw1.addVertexWithUV(d13, d16, d17, d9, d11);
			nw1.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			nw1.addVertexWithUV(d14, d16, d17, d4, d6);
			nw1.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			nw1.addVertexWithUV(d14, d15, d17, d10, d12);
			nw1.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			nw1.addVertexWithUV(d13, d15, d17, d5, d7);
		} else {
			nw1.addVertexWithUV(d13, d16, d17, d9, d11);
			nw1.addVertexWithUV(d14, d16, d17, d4, d6);
			nw1.addVertexWithUV(d14, d15, d17, d10, d12);
			nw1.addVertexWithUV(d13, d15, d17, d5, d7);
		}

	}

	public void renderWestFace(Block uu1, double d1, double d2, double d3, int i1) {
		Tessellator nw1 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		double d4 = ((double)j1 + uu1.minX * 16.0D) / 256.0D;
		double d5 = ((double)j1 + uu1.maxX * 16.0D - 0.01D) / 256.0D;
		double d6 = ((double)(k1 + 16) - uu1.maxY * 16.0D) / 256.0D;
		double d7 = ((double)(k1 + 16) - uu1.minY * 16.0D - 0.01D) / 256.0D;
		double d9;
		if(this.flipTexture) {
			d9 = d4;
			d4 = d5;
			d5 = d9;
		}

		if(uu1.minX < 0.0D || uu1.maxX > 1.0D) {
			d4 = (double)(((float)j1 + 0.0F) / 256.0F);
			d5 = (double)(((float)j1 + 15.99F) / 256.0F);
		}

		if(uu1.minY < 0.0D || uu1.maxY > 1.0D) {
			d6 = (double)(((float)k1 + 0.0F) / 256.0F);
			d7 = (double)(((float)k1 + 15.99F) / 256.0F);
		}

		d9 = d5;
		double d10 = d4;
		double d11 = d6;
		double d12 = d7;
		if(this.field_31086_h == 1) {
			d4 = ((double)j1 + uu1.minY * 16.0D) / 256.0D;
			d7 = ((double)(k1 + 16) - uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)j1 + uu1.maxY * 16.0D) / 256.0D;
			d6 = ((double)(k1 + 16) - uu1.maxX * 16.0D) / 256.0D;
			d11 = d6;
			d12 = d7;
			d9 = d4;
			d10 = d5;
			d6 = d7;
			d7 = d11;
		} else if(this.field_31086_h == 2) {
			d4 = ((double)(j1 + 16) - uu1.maxY * 16.0D) / 256.0D;
			d6 = ((double)k1 + uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.minY * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.maxX * 16.0D) / 256.0D;
			d9 = d5;
			d10 = d4;
			d4 = d5;
			d5 = d10;
			d11 = d7;
			d12 = d6;
		} else if(this.field_31086_h == 3) {
			d4 = ((double)(j1 + 16) - uu1.minX * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.maxX * 16.0D - 0.01D) / 256.0D;
			d6 = ((double)k1 + uu1.maxY * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.minY * 16.0D - 0.01D) / 256.0D;
			d9 = d5;
			d10 = d4;
			d11 = d6;
			d12 = d7;
		}

		double d13 = d1 + uu1.minX;
		double d14 = d1 + uu1.maxX;
		double d15 = d2 + uu1.minY;
		double d16 = d2 + uu1.maxY;
		double d17 = d3 + uu1.maxZ;
		if(this.enableAO) {
			nw1.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			nw1.addVertexWithUV(d13, d16, d17, d4, d6);
			nw1.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			nw1.addVertexWithUV(d13, d15, d17, d10, d12);
			nw1.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			nw1.addVertexWithUV(d14, d15, d17, d5, d7);
			nw1.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			nw1.addVertexWithUV(d14, d16, d17, d9, d11);
		} else {
			nw1.addVertexWithUV(d13, d16, d17, d4, d6);
			nw1.addVertexWithUV(d13, d15, d17, d10, d12);
			nw1.addVertexWithUV(d14, d15, d17, d5, d7);
			nw1.addVertexWithUV(d14, d16, d17, d9, d11);
		}

	}

	public void renderNorthFace(Block uu1, double d1, double d2, double d3, int i1) {
		Tessellator nw1 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		double d4 = ((double)j1 + uu1.minZ * 16.0D) / 256.0D;
		double d5 = ((double)j1 + uu1.maxZ * 16.0D - 0.01D) / 256.0D;
		double d6 = ((double)(k1 + 16) - uu1.maxY * 16.0D) / 256.0D;
		double d7 = ((double)(k1 + 16) - uu1.minY * 16.0D - 0.01D) / 256.0D;
		double d9;
		if(this.flipTexture) {
			d9 = d4;
			d4 = d5;
			d5 = d9;
		}

		if(uu1.minZ < 0.0D || uu1.maxZ > 1.0D) {
			d4 = (double)(((float)j1 + 0.0F) / 256.0F);
			d5 = (double)(((float)j1 + 15.99F) / 256.0F);
		}

		if(uu1.minY < 0.0D || uu1.maxY > 1.0D) {
			d6 = (double)(((float)k1 + 0.0F) / 256.0F);
			d7 = (double)(((float)k1 + 15.99F) / 256.0F);
		}

		d9 = d5;
		double d10 = d4;
		double d11 = d6;
		double d12 = d7;
		if(this.field_31084_j == 1) {
			d4 = ((double)j1 + uu1.minY * 16.0D) / 256.0D;
			d6 = ((double)(k1 + 16) - uu1.maxZ * 16.0D) / 256.0D;
			d5 = ((double)j1 + uu1.maxY * 16.0D) / 256.0D;
			d7 = ((double)(k1 + 16) - uu1.minZ * 16.0D) / 256.0D;
			d11 = d6;
			d12 = d7;
			d9 = d4;
			d10 = d5;
			d6 = d7;
			d7 = d11;
		} else if(this.field_31084_j == 2) {
			d4 = ((double)(j1 + 16) - uu1.maxY * 16.0D) / 256.0D;
			d6 = ((double)k1 + uu1.minZ * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.minY * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.maxZ * 16.0D) / 256.0D;
			d9 = d5;
			d10 = d4;
			d4 = d5;
			d5 = d10;
			d11 = d7;
			d12 = d6;
		} else if(this.field_31084_j == 3) {
			d4 = ((double)(j1 + 16) - uu1.minZ * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.maxZ * 16.0D - 0.01D) / 256.0D;
			d6 = ((double)k1 + uu1.maxY * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.minY * 16.0D - 0.01D) / 256.0D;
			d9 = d5;
			d10 = d4;
			d11 = d6;
			d12 = d7;
		}

		double d13 = d1 + uu1.minX;
		double d14 = d2 + uu1.minY;
		double d15 = d2 + uu1.maxY;
		double d16 = d3 + uu1.minZ;
		double d17 = d3 + uu1.maxZ;
		if(this.enableAO) {
			nw1.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			nw1.addVertexWithUV(d13, d15, d17, d9, d11);
			nw1.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			nw1.addVertexWithUV(d13, d15, d16, d4, d6);
			nw1.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			nw1.addVertexWithUV(d13, d14, d16, d10, d12);
			nw1.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			nw1.addVertexWithUV(d13, d14, d17, d5, d7);
		} else {
			nw1.addVertexWithUV(d13, d15, d17, d9, d11);
			nw1.addVertexWithUV(d13, d15, d16, d4, d6);
			nw1.addVertexWithUV(d13, d14, d16, d10, d12);
			nw1.addVertexWithUV(d13, d14, d17, d5, d7);
		}

	}

	public void renderSouthFace(Block uu1, double d1, double d2, double d3, int i1) {
		Tessellator nw1 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i1 = this.overrideBlockTexture;
		}

		int j1 = (i1 & 15) << 4;
		int k1 = i1 & 240;
		double d4 = ((double)j1 + uu1.minZ * 16.0D) / 256.0D;
		double d5 = ((double)j1 + uu1.maxZ * 16.0D - 0.01D) / 256.0D;
		double d6 = ((double)(k1 + 16) - uu1.maxY * 16.0D) / 256.0D;
		double d7 = ((double)(k1 + 16) - uu1.minY * 16.0D - 0.01D) / 256.0D;
		double d9;
		if(this.flipTexture) {
			d9 = d4;
			d4 = d5;
			d5 = d9;
		}

		if(uu1.minZ < 0.0D || uu1.maxZ > 1.0D) {
			d4 = (double)(((float)j1 + 0.0F) / 256.0F);
			d5 = (double)(((float)j1 + 15.99F) / 256.0F);
		}

		if(uu1.minY < 0.0D || uu1.maxY > 1.0D) {
			d6 = (double)(((float)k1 + 0.0F) / 256.0F);
			d7 = (double)(((float)k1 + 15.99F) / 256.0F);
		}

		d9 = d5;
		double d10 = d4;
		double d11 = d6;
		double d12 = d7;
		if(this.field_31085_i == 2) {
			d4 = ((double)j1 + uu1.minY * 16.0D) / 256.0D;
			d6 = ((double)(k1 + 16) - uu1.minZ * 16.0D) / 256.0D;
			d5 = ((double)j1 + uu1.maxY * 16.0D) / 256.0D;
			d7 = ((double)(k1 + 16) - uu1.maxZ * 16.0D) / 256.0D;
			d11 = d6;
			d12 = d7;
			d9 = d4;
			d10 = d5;
			d6 = d7;
			d7 = d11;
		} else if(this.field_31085_i == 1) {
			d4 = ((double)(j1 + 16) - uu1.maxY * 16.0D) / 256.0D;
			d6 = ((double)k1 + uu1.maxZ * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.minY * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.minZ * 16.0D) / 256.0D;
			d9 = d5;
			d10 = d4;
			d4 = d5;
			d5 = d10;
			d11 = d7;
			d12 = d6;
		} else if(this.field_31085_i == 3) {
			d4 = ((double)(j1 + 16) - uu1.minZ * 16.0D) / 256.0D;
			d5 = ((double)(j1 + 16) - uu1.maxZ * 16.0D - 0.01D) / 256.0D;
			d6 = ((double)k1 + uu1.maxY * 16.0D) / 256.0D;
			d7 = ((double)k1 + uu1.minY * 16.0D - 0.01D) / 256.0D;
			d9 = d5;
			d10 = d4;
			d11 = d6;
			d12 = d7;
		}

		double d13 = d1 + uu1.maxX;
		double d14 = d2 + uu1.minY;
		double d15 = d2 + uu1.maxY;
		double d16 = d3 + uu1.minZ;
		double d17 = d3 + uu1.maxZ;
		if(this.enableAO) {
			nw1.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
			nw1.addVertexWithUV(d13, d14, d17, d10, d12);
			nw1.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
			nw1.addVertexWithUV(d13, d14, d16, d5, d7);
			nw1.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
			nw1.addVertexWithUV(d13, d15, d16, d9, d11);
			nw1.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
			nw1.addVertexWithUV(d13, d15, d17, d4, d6);
		} else {
			nw1.addVertexWithUV(d13, d14, d17, d10, d12);
			nw1.addVertexWithUV(d13, d14, d16, d5, d7);
			nw1.addVertexWithUV(d13, d15, d16, d9, d11);
			nw1.addVertexWithUV(d13, d15, d17, d4, d6);
		}

	}

	public void renderBlockOnInventory(Block uu1, int i1, float f1) {
		Tessellator nw1 = Tessellator.instance;
		int k1;
		float i2;
		float f5;
		if(this.field_31088_b) {
			k1 = uu1.getRenderColor(i1);
			i2 = (float)(k1 >> 16 & 255) / 255.0F;
			f5 = (float)(k1 >> 8 & 255) / 255.0F;
			float f6 = (float)(k1 & 255) / 255.0F;
			GL11.glColor4f(i2 * f1, f5 * f1, f6 * f1, 1.0F);
		}

		k1 = uu1.getRenderType();
		if(k1 != 0 && k1 != 16) {
			if(k1 == 1) {
				nw1.startDrawingQuads();
				nw1.setNormal(0.0F, -1.0F, 0.0F);
				this.renderCrossedSquares(uu1, i1, -0.5D, -0.5D, -0.5D);
				nw1.draw();
			} else if(k1 == 13) {
				uu1.setBlockBoundsForItemRender();
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				i2 = 0.0625F;
				nw1.startDrawingQuads();
				nw1.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBottomFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(0));
				nw1.draw();
				nw1.startDrawingQuads();
				nw1.setNormal(0.0F, 1.0F, 0.0F);
				this.renderTopFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(1));
				nw1.draw();
				nw1.startDrawingQuads();
				nw1.setNormal(0.0F, 0.0F, -1.0F);
				nw1.setTranslationF(0.0F, 0.0F, i2);
				this.renderEastFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(2));
				nw1.setTranslationF(0.0F, 0.0F, -i2);
				nw1.draw();
				nw1.startDrawingQuads();
				nw1.setNormal(0.0F, 0.0F, 1.0F);
				nw1.setTranslationF(0.0F, 0.0F, -i2);
				this.renderWestFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(3));
				nw1.setTranslationF(0.0F, 0.0F, i2);
				nw1.draw();
				nw1.startDrawingQuads();
				nw1.setNormal(-1.0F, 0.0F, 0.0F);
				nw1.setTranslationF(i2, 0.0F, 0.0F);
				this.renderNorthFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(4));
				nw1.setTranslationF(-i2, 0.0F, 0.0F);
				nw1.draw();
				nw1.startDrawingQuads();
				nw1.setNormal(1.0F, 0.0F, 0.0F);
				nw1.setTranslationF(-i2, 0.0F, 0.0F);
				this.renderSouthFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(5));
				nw1.setTranslationF(i2, 0.0F, 0.0F);
				nw1.draw();
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			} else if(k1 == 6) {
				nw1.startDrawingQuads();
				nw1.setNormal(0.0F, -1.0F, 0.0F);
				this.func_1245_b(uu1, i1, -0.5D, -0.5D, -0.5D);
				nw1.draw();
			} else if(k1 == 2) {
				nw1.startDrawingQuads();
				nw1.setNormal(0.0F, -1.0F, 0.0F);
				this.renderTorchAtAngle(uu1, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
				nw1.draw();
			} else {
				int i9;
				if(k1 == 10) {
					for(i9 = 0; i9 < 2; ++i9) {
						if(i9 == 0) {
							uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
						}

						if(i9 == 1) {
							uu1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
						}

						GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
						nw1.startDrawingQuads();
						nw1.setNormal(0.0F, -1.0F, 0.0F);
						this.renderBottomFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(0));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(0.0F, 1.0F, 0.0F);
						this.renderTopFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(1));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(0.0F, 0.0F, -1.0F);
						this.renderEastFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(2));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(0.0F, 0.0F, 1.0F);
						this.renderWestFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(3));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(-1.0F, 0.0F, 0.0F);
						this.renderNorthFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(4));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(1.0F, 0.0F, 0.0F);
						this.renderSouthFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(5));
						nw1.draw();
						GL11.glTranslatef(0.5F, 0.5F, 0.5F);
					}
				} else if(k1 == 11) {
					for(i9 = 0; i9 < 4; ++i9) {
						f5 = 0.125F;
						if(i9 == 0) {
							uu1.setBlockBounds(0.5F - f5, 0.0F, 0.0F, 0.5F + f5, 1.0F, f5 * 2.0F);
						}

						if(i9 == 1) {
							uu1.setBlockBounds(0.5F - f5, 0.0F, 1.0F - f5 * 2.0F, 0.5F + f5, 1.0F, 1.0F);
						}

						f5 = 0.0625F;
						if(i9 == 2) {
							uu1.setBlockBounds(0.5F - f5, 1.0F - f5 * 3.0F, -f5 * 2.0F, 0.5F + f5, 1.0F - f5, 1.0F + f5 * 2.0F);
						}

						if(i9 == 3) {
							uu1.setBlockBounds(0.5F - f5, 0.5F - f5 * 3.0F, -f5 * 2.0F, 0.5F + f5, 0.5F - f5, 1.0F + f5 * 2.0F);
						}

						GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
						nw1.startDrawingQuads();
						nw1.setNormal(0.0F, -1.0F, 0.0F);
						this.renderBottomFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(0));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(0.0F, 1.0F, 0.0F);
						this.renderTopFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(1));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(0.0F, 0.0F, -1.0F);
						this.renderEastFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(2));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(0.0F, 0.0F, 1.0F);
						this.renderWestFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(3));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(-1.0F, 0.0F, 0.0F);
						this.renderNorthFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(4));
						nw1.draw();
						nw1.startDrawingQuads();
						nw1.setNormal(1.0F, 0.0F, 0.0F);
						this.renderSouthFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSide(5));
						nw1.draw();
						GL11.glTranslatef(0.5F, 0.5F, 0.5F);
					}

					uu1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				} else {
					ModLoader.RenderInvBlock(this, uu1, i1, k1);
				}
			}
		} else {
			if(k1 == 16) {
				i1 = 1;
			}

			uu1.setBlockBoundsForItemRender();
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			nw1.startDrawingQuads();
			nw1.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBottomFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSideAndMetadata(0, i1));
			nw1.draw();
			nw1.startDrawingQuads();
			nw1.setNormal(0.0F, 1.0F, 0.0F);
			this.renderTopFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSideAndMetadata(1, i1));
			nw1.draw();
			nw1.startDrawingQuads();
			nw1.setNormal(0.0F, 0.0F, -1.0F);
			this.renderEastFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSideAndMetadata(2, i1));
			nw1.draw();
			nw1.startDrawingQuads();
			nw1.setNormal(0.0F, 0.0F, 1.0F);
			this.renderWestFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSideAndMetadata(3, i1));
			nw1.draw();
			nw1.startDrawingQuads();
			nw1.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderNorthFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSideAndMetadata(4, i1));
			nw1.draw();
			nw1.startDrawingQuads();
			nw1.setNormal(1.0F, 0.0F, 0.0F);
			this.renderSouthFace(uu1, 0.0D, 0.0D, 0.0D, uu1.getBlockTextureFromSideAndMetadata(5, i1));
			nw1.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}

	}

	public static boolean renderItemIn3d(int i1) {
		return i1 == 0 ? true : (i1 == 13 ? true : (i1 == 10 ? true : (i1 == 11 ? true : ModLoader.RenderBlockIsItemFull3D(i1))));
	}
}
