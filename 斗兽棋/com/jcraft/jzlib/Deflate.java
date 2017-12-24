package com.jcraft.jzlib;

public final class Deflate
{
  private static final int BL_CODES = 19;
  private static final int BUSY_STATE = 113;
  private static final int BlockDone = 1;
  private static final int Buf_size = 16;
  private static final int DEF_MEM_LEVEL = 8;
  private static final int DYN_TREES = 2;
  private static final int D_CODES = 30;
  private static final int END_BLOCK = 256;
  private static final int FAST = 1;
  private static final int FINISH_STATE = 666;
  private static final int FinishDone = 3;
  private static final int FinishStarted = 2;
  private static final int HEAP_SIZE = 573;
  private static final int INIT_STATE = 42;
  private static final int LENGTH_CODES = 29;
  private static final int LITERALS = 256;
  private static final int L_CODES = 286;
  private static final int MAX_BITS = 15;
  private static final int MAX_MATCH = 258;
  private static final int MAX_MEM_LEVEL = 9;
  private static final int MAX_WBITS = 15;
  private static final int MIN_LOOKAHEAD = 262;
  private static final int MIN_MATCH = 3;
  private static final int NeedMore = 0;
  private static final int PRESET_DICT = 32;
  private static final int REPZ_11_138 = 18;
  private static final int REPZ_3_10 = 17;
  private static final int REP_3_6 = 16;
  private static final int SLOW = 2;
  private static final int STATIC_TREES = 1;
  private static final int STORED = 0;
  private static final int STORED_BLOCK = 0;
  private static final int Z_ASCII = 1;
  private static final int Z_BINARY = 0;
  private static final int Z_BUF_ERROR = -5;
  private static final int Z_DATA_ERROR = -3;
  private static final int Z_DEFAULT_COMPRESSION = -1;
  private static final int Z_DEFAULT_STRATEGY = 0;
  private static final int Z_DEFLATED = 8;
  private static final int Z_ERRNO = -1;
  private static final int Z_FILTERED = 1;
  private static final int Z_FINISH = 4;
  private static final int Z_FULL_FLUSH = 3;
  private static final int Z_HUFFMAN_ONLY = 2;
  private static final int Z_MEM_ERROR = -4;
  private static final int Z_NEED_DICT = 2;
  private static final int Z_NO_FLUSH = 0;
  private static final int Z_OK = 0;
  private static final int Z_PARTIAL_FLUSH = 1;
  private static final int Z_STREAM_END = 1;
  private static final int Z_STREAM_ERROR = -2;
  private static final int Z_SYNC_FLUSH = 2;
  private static final int Z_UNKNOWN = 2;
  private static final int Z_VERSION_ERROR = -6;
  private static final Config[] config_table = new Config[10];
  private static final String[] z_errmsg = { "need dictionary", "stream end", "", "file error", "stream error", "data error", "insufficient memory", "buffer error", "incompatible version", "" };
  short bi_buf;
  int bi_valid;
  short[] bl_count = new short[16];
  Tree bl_desc = new Tree();
  short[] bl_tree = new short[78];
  int block_start;
  int d_buf;
  Tree d_desc = new Tree();
  byte data_type;
  byte[] depth = new byte[573];
  short[] dyn_dtree = new short[122];
  short[] dyn_ltree = new short[1146];
  int good_match;
  int hash_bits;
  int hash_mask;
  int hash_shift;
  int hash_size;
  short[] head;
  int[] heap = new int[573];
  int heap_len;
  int heap_max;
  int ins_h;
  int l_buf;
  Tree l_desc = new Tree();
  int last_eob_len;
  int last_flush;
  int last_lit;
  int level;
  int lit_bufsize;
  int lookahead;
  int match_available;
  int match_length;
  int match_start;
  int matches;
  int max_chain_length;
  int max_lazy_match;
  byte method;
  int nice_match;
  int noheader;
  int opt_len;
  int pending;
  byte[] pending_buf;
  int pending_buf_size;
  int pending_out;
  short[] prev;
  int prev_length;
  int prev_match;
  int static_len;
  int status;
  int strategy;
  ZStream strm;
  int strstart;
  int w_bits;
  int w_mask;
  int w_size;
  byte[] window;
  int window_size;
  
  static
  {
    config_table[0] = new Config(0, 0, 0, 0, 0);
    config_table[1] = new Config(4, 4, 8, 4, 1);
    config_table[2] = new Config(4, 5, 16, 8, 1);
    config_table[3] = new Config(4, 6, 32, 32, 1);
    config_table[4] = new Config(4, 4, 16, 16, 2);
    config_table[5] = new Config(8, 16, 32, 32, 2);
    config_table[6] = new Config(8, 16, 128, 128, 2);
    config_table[7] = new Config(8, 32, 128, 256, 2);
    config_table[8] = new Config(32, 128, 258, 1024, 2);
    config_table[9] = new Config(32, 258, 258, 4096, 2);
  }
  
  static boolean smaller(short[] paramArrayOfShort, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfShort[(paramInt1 * 2)];
    int j = paramArrayOfShort[(paramInt2 * 2)];
    return (i < j) || ((i == j) && (paramArrayOfByte[paramInt1] <= paramArrayOfByte[paramInt2]));
  }
  
  void _tr_align()
  {
    send_bits(2, 3);
    send_code(256, StaticTree.static_ltree);
    bi_flush();
    if (this.last_eob_len + 1 + 10 - this.bi_valid < 9)
    {
      send_bits(2, 3);
      send_code(256, StaticTree.static_ltree);
      bi_flush();
    }
    this.last_eob_len = 7;
  }
  
  void _tr_flush_block(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int j = 0;
    int i;
    int m;
    if (this.level > 0)
    {
      if (this.data_type == 2) {
        set_data_type();
      }
      this.l_desc.build_tree(this);
      this.d_desc.build_tree(this);
      int n = build_bl_tree();
      int i1 = this.opt_len + 3 + 7 >>> 3;
      int k = this.static_len + 3 + 7 >>> 3;
      j = n;
      i = i1;
      m = k;
      if (k <= i1)
      {
        i = k;
        m = k;
        j = n;
      }
    }
    while ((paramInt2 + 4 <= i) && (paramInt1 != -1))
    {
      _tr_stored_block(paramInt1, paramInt2, paramBoolean);
      init_block();
      if (paramBoolean) {
        bi_windup();
      }
      return;
      m = paramInt2 + 5;
      i = m;
    }
    if (m == i)
    {
      if (paramBoolean) {}
      for (paramInt1 = 1;; paramInt1 = 0)
      {
        send_bits(paramInt1 + 2, 3);
        compress_block(StaticTree.static_ltree, StaticTree.static_dtree);
        break;
      }
    }
    if (paramBoolean) {}
    for (paramInt1 = 1;; paramInt1 = 0)
    {
      send_bits(paramInt1 + 4, 3);
      send_all_trees(this.l_desc.max_code + 1, this.d_desc.max_code + 1, j + 1);
      compress_block(this.dyn_ltree, this.dyn_dtree);
      break;
    }
  }
  
  void _tr_stored_block(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 1;; i = 0)
    {
      send_bits(i + 0, 3);
      copy_block(paramInt1, paramInt2, true);
      return;
    }
  }
  
  boolean _tr_tally(int paramInt1, int paramInt2)
  {
    this.pending_buf[(this.d_buf + this.last_lit * 2)] = ((byte)(paramInt1 >>> 8));
    this.pending_buf[(this.d_buf + this.last_lit * 2 + 1)] = ((byte)paramInt1);
    this.pending_buf[(this.l_buf + this.last_lit)] = ((byte)paramInt2);
    this.last_lit += 1;
    short[] arrayOfShort;
    if (paramInt1 == 0)
    {
      arrayOfShort = this.dyn_ltree;
      paramInt1 = paramInt2 * 2;
      arrayOfShort[paramInt1] = ((short)(arrayOfShort[paramInt1] + 1));
    }
    while (((this.last_lit & 0x1FFF) == 0) && (this.level > 2))
    {
      paramInt2 = this.last_lit * 8;
      int i = this.strstart;
      int j = this.block_start;
      paramInt1 = 0;
      for (;;)
      {
        if (paramInt1 < 30)
        {
          paramInt2 = (int)(paramInt2 + this.dyn_dtree[(paramInt1 * 2)] * (5L + Tree.extra_dbits[paramInt1]));
          paramInt1 += 1;
          continue;
          this.matches += 1;
          arrayOfShort = this.dyn_ltree;
          paramInt2 = (Tree._length_code[paramInt2] + 256 + 1) * 2;
          arrayOfShort[paramInt2] = ((short)(arrayOfShort[paramInt2] + 1));
          arrayOfShort = this.dyn_dtree;
          paramInt1 = Tree.d_code(paramInt1 - 1) * 2;
          arrayOfShort[paramInt1] = ((short)(arrayOfShort[paramInt1] + 1));
          break;
        }
      }
      if ((this.matches < this.last_lit / 2) && (paramInt2 >>> 3 < (i - j) / 2)) {
        return true;
      }
    }
    return this.last_lit == this.lit_bufsize - 1;
  }
  
  void bi_flush()
  {
    if (this.bi_valid == 16)
    {
      put_short(this.bi_buf);
      this.bi_buf = 0;
      this.bi_valid = 0;
    }
    while (this.bi_valid < 8) {
      return;
    }
    put_byte((byte)this.bi_buf);
    this.bi_buf = ((short)(this.bi_buf >>> 8));
    this.bi_valid -= 8;
  }
  
  void bi_windup()
  {
    if (this.bi_valid > 8) {
      put_short(this.bi_buf);
    }
    for (;;)
    {
      this.bi_buf = 0;
      this.bi_valid = 0;
      return;
      if (this.bi_valid > 0) {
        put_byte((byte)this.bi_buf);
      }
    }
  }
  
  int build_bl_tree()
  {
    scan_tree(this.dyn_ltree, this.l_desc.max_code);
    scan_tree(this.dyn_dtree, this.d_desc.max_code);
    this.bl_desc.build_tree(this);
    int i = 18;
    for (;;)
    {
      if ((i < 3) || (this.bl_tree[(Tree.bl_order[i] * 2 + 1)] != 0))
      {
        this.opt_len += (i + 1) * 3 + 5 + 5 + 4;
        return i;
      }
      i -= 1;
    }
  }
  
  void compress_block(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
  {
    int i = 0;
    int k;
    int m;
    int j;
    if (this.last_lit != 0)
    {
      k = this.pending_buf[(this.d_buf + i * 2)] << 8 & 0xFF00 | this.pending_buf[(this.d_buf + i * 2 + 1)] & 0xFF;
      m = this.pending_buf[(this.l_buf + i)] & 0xFF;
      j = i + 1;
      if (k != 0) {
        break label115;
      }
      send_code(m, paramArrayOfShort1);
    }
    for (;;)
    {
      i = j;
      if (j < this.last_lit) {
        break;
      }
      send_code(256, paramArrayOfShort1);
      this.last_eob_len = paramArrayOfShort1[513];
      return;
      label115:
      i = Tree._length_code[m];
      send_code(i + 256 + 1, paramArrayOfShort1);
      int n = Tree.extra_lbits[i];
      if (n != 0) {
        send_bits(m - Tree.base_length[i], n);
      }
      i = k - 1;
      k = Tree.d_code(i);
      send_code(k, paramArrayOfShort2);
      m = Tree.extra_dbits[k];
      if (m != 0) {
        send_bits(i - Tree.base_dist[k], m);
      }
    }
  }
  
  void copy_block(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    bi_windup();
    this.last_eob_len = 8;
    if (paramBoolean)
    {
      put_short((short)paramInt2);
      put_short((short)(paramInt2 ^ 0xFFFFFFFF));
    }
    put_byte(this.window, paramInt1, paramInt2);
  }
  
  int deflate(ZStream paramZStream, int paramInt)
  {
    if ((paramInt > 4) || (paramInt < 0)) {
      return -2;
    }
    if ((paramZStream.next_out == null) || ((paramZStream.next_in == null) && (paramZStream.avail_in != 0)) || ((this.status == 666) && (paramInt != 4)))
    {
      paramZStream.msg = z_errmsg[4];
      return -2;
    }
    if (paramZStream.avail_out == 0)
    {
      paramZStream.msg = z_errmsg[7];
      return -5;
    }
    this.strm = paramZStream;
    int k = this.last_flush;
    this.last_flush = paramInt;
    int i;
    if (this.status == 42)
    {
      int m = this.w_bits;
      int j = (this.level - 1 & 0xFF) >> 1;
      i = j;
      if (j > 3) {
        i = 3;
      }
      j = (m - 8 << 4) + 8 << 8 | i << 6;
      i = j;
      if (this.strstart != 0) {
        i = j | 0x20;
      }
      this.status = 113;
      putShortMSB(i + (31 - i % 31));
      if (this.strstart != 0)
      {
        putShortMSB((int)(paramZStream.adler >>> 16));
        putShortMSB((int)(paramZStream.adler & 0xFFFF));
      }
      paramZStream.adler = paramZStream._adler.adler32(0L, null, 0, 0);
    }
    if (this.pending != 0)
    {
      paramZStream.flush_pending();
      if (paramZStream.avail_out == 0)
      {
        this.last_flush = -1;
        return 0;
      }
    }
    else if ((paramZStream.avail_in == 0) && (paramInt <= k) && (paramInt != 4))
    {
      paramZStream.msg = z_errmsg[7];
      return -5;
    }
    if ((this.status == 666) && (paramZStream.avail_in != 0))
    {
      paramZStream.msg = z_errmsg[7];
      return -5;
    }
    if ((paramZStream.avail_in != 0) || (this.lookahead != 0) || ((paramInt != 0) && (this.status != 666)))
    {
      i = -1;
      switch (config_table[this.level].func)
      {
      }
      for (;;)
      {
        if ((i == 2) || (i == 3)) {
          this.status = 666;
        }
        if ((i != 0) && (i != 2)) {
          break;
        }
        if (paramZStream.avail_out == 0) {
          this.last_flush = -1;
        }
        return 0;
        i = deflate_stored(paramInt);
        continue;
        i = deflate_fast(paramInt);
        continue;
        i = deflate_slow(paramInt);
      }
      if (i == 1)
      {
        if (paramInt == 1) {
          _tr_align();
        }
        for (;;)
        {
          paramZStream.flush_pending();
          if (paramZStream.avail_out != 0) {
            break;
          }
          this.last_flush = -1;
          return 0;
          _tr_stored_block(0, 0, false);
          if (paramInt == 3)
          {
            i = 0;
            while (i < this.hash_size)
            {
              this.head[i] = 0;
              i += 1;
            }
          }
        }
      }
    }
    if (paramInt != 4) {
      return 0;
    }
    if (this.noheader != 0) {
      return 1;
    }
    putShortMSB((int)(paramZStream.adler >>> 16));
    putShortMSB((int)(paramZStream.adler & 0xFFFF));
    paramZStream.flush_pending();
    this.noheader = -1;
    if (this.pending != 0) {
      return 0;
    }
    return 1;
  }
  
  int deflateEnd()
  {
    if ((this.status != 42) && (this.status != 113) && (this.status != 666)) {
      return -2;
    }
    this.pending_buf = null;
    this.head = null;
    this.prev = null;
    this.window = null;
    if (this.status == 113) {
      return -3;
    }
    return 0;
  }
  
  int deflateInit(ZStream paramZStream, int paramInt)
  {
    return deflateInit(paramZStream, paramInt, 15);
  }
  
  int deflateInit(ZStream paramZStream, int paramInt1, int paramInt2)
  {
    return deflateInit2(paramZStream, paramInt1, 8, paramInt2, 8, 0);
  }
  
  int deflateInit2(ZStream paramZStream, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int j = 0;
    paramZStream.msg = null;
    int i = paramInt1;
    if (paramInt1 == -1) {
      i = 6;
    }
    paramInt1 = paramInt3;
    if (paramInt3 < 0)
    {
      j = 1;
      paramInt1 = -paramInt3;
    }
    if ((paramInt4 < 1) || (paramInt4 > 9) || (paramInt2 != 8) || (paramInt1 < 9) || (paramInt1 > 15) || (i < 0) || (i > 9) || (paramInt5 < 0) || (paramInt5 > 2)) {
      return -2;
    }
    paramZStream.dstate = this;
    this.noheader = j;
    this.w_bits = paramInt1;
    this.w_size = (1 << this.w_bits);
    this.w_mask = (this.w_size - 1);
    this.hash_bits = (paramInt4 + 7);
    this.hash_size = (1 << this.hash_bits);
    this.hash_mask = (this.hash_size - 1);
    this.hash_shift = ((this.hash_bits + 3 - 1) / 3);
    this.window = new byte[this.w_size * 2];
    this.prev = new short[this.w_size];
    this.head = new short[this.hash_size];
    this.lit_bufsize = (1 << paramInt4 + 6);
    this.pending_buf = new byte[this.lit_bufsize * 4];
    this.pending_buf_size = (this.lit_bufsize * 4);
    this.d_buf = (this.lit_bufsize / 2);
    this.l_buf = (this.lit_bufsize * 3);
    this.level = i;
    this.strategy = paramInt5;
    this.method = ((byte)paramInt2);
    return deflateReset(paramZStream);
  }
  
  int deflateParams(ZStream paramZStream, int paramInt1, int paramInt2)
  {
    int j = 0;
    int i = paramInt1;
    if (paramInt1 == -1) {
      i = 6;
    }
    if ((i < 0) || (i > 9) || (paramInt2 < 0) || (paramInt2 > 2)) {
      return -2;
    }
    paramInt1 = j;
    if (config_table[this.level].func != config_table[i].func)
    {
      paramInt1 = j;
      if (paramZStream.total_in != 0L) {
        paramInt1 = paramZStream.deflate(1);
      }
    }
    if (this.level != i)
    {
      this.level = i;
      this.max_lazy_match = config_table[this.level].max_lazy;
      this.good_match = config_table[this.level].good_length;
      this.nice_match = config_table[this.level].nice_length;
      this.max_chain_length = config_table[this.level].max_chain;
    }
    this.strategy = paramInt2;
    return paramInt1;
  }
  
  int deflateReset(ZStream paramZStream)
  {
    paramZStream.total_out = 0L;
    paramZStream.total_in = 0L;
    paramZStream.msg = null;
    paramZStream.data_type = 2;
    this.pending = 0;
    this.pending_out = 0;
    if (this.noheader < 0) {
      this.noheader = 0;
    }
    if (this.noheader != 0) {}
    for (int i = 113;; i = 42)
    {
      this.status = i;
      paramZStream.adler = paramZStream._adler.adler32(0L, null, 0, 0);
      this.last_flush = 0;
      tr_init();
      lm_init();
      return 0;
    }
  }
  
  int deflateSetDictionary(ZStream paramZStream, byte[] paramArrayOfByte, int paramInt)
  {
    int j = paramInt;
    int k = 0;
    if ((paramArrayOfByte == null) || (this.status != 42)) {
      return -2;
    }
    paramZStream.adler = paramZStream._adler.adler32(paramZStream.adler, paramArrayOfByte, 0, paramInt);
    if (j < 3) {
      return 0;
    }
    int i = j;
    if (j > this.w_size - 262)
    {
      i = this.w_size - 262;
      k = paramInt - i;
    }
    System.arraycopy(paramArrayOfByte, k, this.window, 0, i);
    this.strstart = i;
    this.block_start = i;
    this.ins_h = (this.window[0] & 0xFF);
    this.ins_h = ((this.ins_h << this.hash_shift ^ this.window[1] & 0xFF) & this.hash_mask);
    paramInt = 0;
    while (paramInt <= i - 3)
    {
      this.ins_h = ((this.ins_h << this.hash_shift ^ this.window[(paramInt + 2)] & 0xFF) & this.hash_mask);
      this.prev[(this.w_mask & paramInt)] = this.head[this.ins_h];
      this.head[this.ins_h] = ((short)paramInt);
      paramInt += 1;
    }
    return 0;
  }
  
  int deflate_fast(int paramInt)
  {
    int i = 0;
    if (this.lookahead < 262)
    {
      fill_window();
      if ((this.lookahead < 262) && (paramInt == 0)) {
        return 0;
      }
      if (this.lookahead == 0) {
        if (paramInt != 4) {
          break label545;
        }
      }
    }
    label545:
    for (boolean bool = true;; bool = false)
    {
      flush_block_only(bool);
      if (this.strm.avail_out != 0) {
        break label553;
      }
      if (paramInt != 4) {
        break label551;
      }
      return 2;
      if (this.lookahead >= 3)
      {
        this.ins_h = ((this.ins_h << this.hash_shift ^ this.window[(this.strstart + 2)] & 0xFF) & this.hash_mask);
        i = this.head[this.ins_h] & 0xFFFF;
        this.prev[(this.strstart & this.w_mask)] = this.head[this.ins_h];
        this.head[this.ins_h] = ((short)this.strstart);
      }
      if ((i != 0L) && ((this.strstart - i & 0xFFFF) <= this.w_size - 262) && (this.strategy != 2)) {
        this.match_length = longest_match(i);
      }
      int j;
      if (this.match_length >= 3)
      {
        bool = _tr_tally(this.strstart - this.match_start, this.match_length - 3);
        this.lookahead -= this.match_length;
        if ((this.match_length <= this.max_lazy_match) && (this.lookahead >= 3))
        {
          this.match_length -= 1;
          do
          {
            this.strstart += 1;
            this.ins_h = ((this.ins_h << this.hash_shift ^ this.window[(this.strstart + 2)] & 0xFF) & this.hash_mask);
            j = this.head[this.ins_h] & 0xFFFF;
            this.prev[(this.strstart & this.w_mask)] = this.head[this.ins_h];
            this.head[this.ins_h] = ((short)this.strstart);
            i = this.match_length - 1;
            this.match_length = i;
          } while (i != 0);
          this.strstart += 1;
        }
      }
      for (;;)
      {
        i = j;
        if (!bool) {
          break;
        }
        flush_block_only(false);
        i = j;
        if (this.strm.avail_out != 0) {
          break;
        }
        return 0;
        this.strstart += this.match_length;
        this.match_length = 0;
        this.ins_h = (this.window[this.strstart] & 0xFF);
        this.ins_h = ((this.ins_h << this.hash_shift ^ this.window[(this.strstart + 1)] & 0xFF) & this.hash_mask);
        j = i;
        continue;
        bool = _tr_tally(0, this.window[this.strstart] & 0xFF);
        this.lookahead -= 1;
        this.strstart += 1;
        j = i;
      }
    }
    label551:
    return 0;
    label553:
    if (paramInt == 4) {
      return 3;
    }
    return 1;
  }
  
  int deflate_slow(int paramInt)
  {
    int j = 0;
    if (this.lookahead < 262)
    {
      fill_window();
      if ((this.lookahead < 262) && (paramInt == 0)) {
        return 0;
      }
      if (this.lookahead == 0)
      {
        if (this.match_available != 0)
        {
          _tr_tally(0, this.window[(this.strstart - 1)] & 0xFF);
          this.match_available = 0;
        }
        if (paramInt != 4) {
          break label676;
        }
      }
    }
    label676:
    for (boolean bool = true;; bool = false)
    {
      flush_block_only(bool);
      if (this.strm.avail_out != 0) {
        break label684;
      }
      if (paramInt != 4) {
        break label682;
      }
      return 2;
      int i = j;
      if (this.lookahead >= 3)
      {
        this.ins_h = ((this.ins_h << this.hash_shift ^ this.window[(this.strstart + 2)] & 0xFF) & this.hash_mask);
        i = this.head[this.ins_h] & 0xFFFF;
        this.prev[(this.strstart & this.w_mask)] = this.head[this.ins_h];
        this.head[this.ins_h] = ((short)this.strstart);
      }
      this.prev_length = this.match_length;
      this.prev_match = this.match_start;
      this.match_length = 2;
      if ((i != 0) && (this.prev_length < this.max_lazy_match) && ((this.strstart - i & 0xFFFF) <= this.w_size - 262))
      {
        if (this.strategy != 2) {
          this.match_length = longest_match(i);
        }
        if ((this.match_length <= 5) && ((this.strategy == 1) || ((this.match_length == 3) && (this.strstart - this.match_start > 4096)))) {
          this.match_length = 2;
        }
      }
      if ((this.prev_length >= 3) && (this.match_length <= this.prev_length))
      {
        int k = this.strstart;
        int m = this.lookahead;
        bool = _tr_tally(this.strstart - 1 - this.prev_match, this.prev_length - 3);
        this.lookahead -= this.prev_length - 1;
        this.prev_length -= 2;
        j = i;
        int n;
        do
        {
          n = this.strstart + 1;
          this.strstart = n;
          i = j;
          if (n <= k + m - 3)
          {
            this.ins_h = ((this.ins_h << this.hash_shift ^ this.window[(this.strstart + 2)] & 0xFF) & this.hash_mask);
            i = this.head[this.ins_h] & 0xFFFF;
            this.prev[(this.strstart & this.w_mask)] = this.head[this.ins_h];
            this.head[this.ins_h] = ((short)this.strstart);
          }
          n = this.prev_length - 1;
          this.prev_length = n;
          j = i;
        } while (n != 0);
        this.match_available = 0;
        this.match_length = 2;
        this.strstart += 1;
        j = i;
        if (!bool) {
          break;
        }
        flush_block_only(false);
        j = i;
        if (this.strm.avail_out != 0) {
          break;
        }
        return 0;
      }
      if (this.match_available != 0)
      {
        if (_tr_tally(0, this.window[(this.strstart - 1)] & 0xFF)) {
          flush_block_only(false);
        }
        this.strstart += 1;
        this.lookahead -= 1;
        j = i;
        if (this.strm.avail_out != 0) {
          break;
        }
        return 0;
      }
      this.match_available = 1;
      this.strstart += 1;
      this.lookahead -= 1;
      j = i;
      break;
    }
    label682:
    return 0;
    label684:
    if (paramInt == 4) {
      return 3;
    }
    return 1;
  }
  
  int deflate_stored(int paramInt)
  {
    int i = 65535;
    if (65535 > this.pending_buf_size - 5) {
      i = this.pending_buf_size - 5;
    }
    if (this.lookahead <= 1)
    {
      fill_window();
      if ((this.lookahead == 0) && (paramInt == 0)) {
        return 0;
      }
      if (this.lookahead == 0) {
        if (paramInt != 4) {
          break label195;
        }
      }
    }
    label195:
    for (boolean bool = true;; bool = false)
    {
      flush_block_only(bool);
      if (this.strm.avail_out != 0) {
        break label203;
      }
      if (paramInt != 4) {
        break label201;
      }
      return 2;
      this.strstart += this.lookahead;
      this.lookahead = 0;
      int j = this.block_start + i;
      if ((this.strstart == 0) || (this.strstart >= j))
      {
        this.lookahead = (this.strstart - j);
        this.strstart = j;
        flush_block_only(false);
        if (this.strm.avail_out == 0) {
          return 0;
        }
      }
      if (this.strstart - this.block_start < this.w_size - 262) {
        break;
      }
      flush_block_only(false);
      if (this.strm.avail_out != 0) {
        break;
      }
      return 0;
    }
    label201:
    return 0;
    label203:
    if (paramInt == 4) {
      return 3;
    }
    return 1;
  }
  
  void fill_window()
  {
    label198:
    label223:
    do
    {
      int m = this.window_size - this.lookahead - this.strstart;
      int j;
      if ((m == 0) && (this.strstart == 0) && (this.lookahead == 0))
      {
        j = this.w_size;
        if (this.strm.avail_in != 0) {
          break label306;
        }
      }
      do
      {
        return;
        if (m == -1)
        {
          j = m - 1;
          break;
        }
        j = m;
        if (this.strstart < this.w_size + this.w_size - 262) {
          break;
        }
        System.arraycopy(this.window, this.w_size, this.window, 0, this.w_size);
        this.match_start -= this.w_size;
        this.strstart -= this.w_size;
        this.block_start -= this.w_size;
        j = this.hash_size;
        int k = j;
        do
        {
          arrayOfShort = this.head;
          k -= 1;
          n = arrayOfShort[k] & 0xFFFF;
          arrayOfShort = this.head;
          if (n < this.w_size) {
            break;
          }
          i = (short)(n - this.w_size);
          arrayOfShort[k] = i;
          n = j - 1;
          j = n;
        } while (n != 0);
        j = this.w_size;
        k = j;
        short[] arrayOfShort = this.prev;
        k -= 1;
        int n = arrayOfShort[k] & 0xFFFF;
        arrayOfShort = this.prev;
        if (n >= this.w_size) {}
        for (int i = (short)(n - this.w_size);; i = 0)
        {
          arrayOfShort[k] = i;
          n = j - 1;
          j = n;
          if (n != 0) {
            break label223;
          }
          j = m + this.w_size;
          break;
          i = 0;
          break label198;
        }
        j = this.strm.read_buf(this.window, this.strstart + this.lookahead, j);
        this.lookahead += j;
        if (this.lookahead >= 3)
        {
          this.ins_h = (this.window[this.strstart] & 0xFF);
          this.ins_h = ((this.ins_h << this.hash_shift ^ this.window[(this.strstart + 1)] & 0xFF) & this.hash_mask);
        }
      } while (this.lookahead >= 262);
    } while (this.strm.avail_in != 0);
    label306:
  }
  
  void flush_block_only(boolean paramBoolean)
  {
    if (this.block_start >= 0) {}
    for (int i = this.block_start;; i = -1)
    {
      _tr_flush_block(i, this.strstart - this.block_start, paramBoolean);
      this.block_start = this.strstart;
      this.strm.flush_pending();
      return;
    }
  }
  
  void init_block()
  {
    int i = 0;
    while (i < 286)
    {
      this.dyn_ltree[(i * 2)] = 0;
      i += 1;
    }
    i = 0;
    while (i < 30)
    {
      this.dyn_dtree[(i * 2)] = 0;
      i += 1;
    }
    i = 0;
    while (i < 19)
    {
      this.bl_tree[(i * 2)] = 0;
      i += 1;
    }
    this.dyn_ltree[512] = 1;
    this.static_len = 0;
    this.opt_len = 0;
    this.matches = 0;
    this.last_lit = 0;
  }
  
  void lm_init()
  {
    this.window_size = (this.w_size * 2);
    this.head[(this.hash_size - 1)] = 0;
    int i = 0;
    while (i < this.hash_size - 1)
    {
      this.head[i] = 0;
      i += 1;
    }
    this.max_lazy_match = config_table[this.level].max_lazy;
    this.good_match = config_table[this.level].good_length;
    this.nice_match = config_table[this.level].nice_length;
    this.max_chain_length = config_table[this.level].max_chain;
    this.strstart = 0;
    this.block_start = 0;
    this.lookahead = 0;
    this.prev_length = 2;
    this.match_length = 2;
    this.match_available = 0;
    this.ins_h = 0;
  }
  
  int longest_match(int paramInt)
  {
    int k = this.max_chain_length;
    int i5 = this.strstart;
    int i6 = this.prev_length;
    int j;
    int i10;
    int i11;
    int i7;
    int i;
    int i3;
    int m;
    int i4;
    int i1;
    int i2;
    int n;
    label177:
    byte[] arrayOfByte;
    if (this.strstart > this.w_size - 262)
    {
      j = this.strstart - (this.w_size - 262);
      int i9 = this.nice_match;
      i10 = this.w_mask;
      i11 = this.strstart + 258;
      i7 = this.window[(i5 + i6 - 1)];
      int i8 = this.window[(i5 + i6)];
      i = k;
      if (this.prev_length >= this.good_match) {
        i = k >> 2;
      }
      i3 = i6;
      k = i;
      m = i9;
      i4 = i5;
      i1 = i8;
      i2 = i7;
      n = paramInt;
      if (i9 > this.lookahead)
      {
        m = this.lookahead;
        n = paramInt;
        i2 = i7;
        i1 = i8;
        i4 = i5;
        k = i;
        i3 = i6;
      }
      paramInt = i3;
      i = i4;
      i5 = i1;
      i6 = i2;
      if (this.window[(n + i3)] == i1)
      {
        paramInt = i3;
        i = i4;
        i5 = i1;
        i6 = i2;
        if (this.window[(n + i3 - 1)] == i2)
        {
          paramInt = i3;
          i = i4;
          i5 = i1;
          i6 = i2;
          if (this.window[n] == this.window[i4])
          {
            arrayOfByte = this.window;
            paramInt = n + 1;
            if (arrayOfByte[paramInt] == this.window[(i4 + 1)]) {
              break label383;
            }
            i6 = i2;
            i5 = i1;
            i = i4;
            paramInt = i3;
          }
        }
      }
    }
    for (;;)
    {
      label309:
      n = this.prev[(n & i10)] & 0xFFFF;
      i1 = paramInt;
      if (n > j)
      {
        i7 = k - 1;
        i3 = paramInt;
        k = i7;
        i4 = i;
        i1 = i5;
        i2 = i6;
        if (i7 != 0) {
          break label177;
        }
        i1 = paramInt;
      }
      label383:
      do
      {
        if (i1 > this.lookahead) {
          break label815;
        }
        return i1;
        j = 0;
        break;
        i4 += 2;
        paramInt += 1;
        do
        {
          arrayOfByte = this.window;
          i = i4 + 1;
          i5 = arrayOfByte[i];
          arrayOfByte = this.window;
          i4 = paramInt + 1;
          paramInt = i;
          if (i5 != arrayOfByte[i4]) {
            break;
          }
          arrayOfByte = this.window;
          i += 1;
          i5 = arrayOfByte[i];
          arrayOfByte = this.window;
          i4 += 1;
          paramInt = i;
          if (i5 != arrayOfByte[i4]) {
            break;
          }
          arrayOfByte = this.window;
          i += 1;
          i5 = arrayOfByte[i];
          arrayOfByte = this.window;
          i4 += 1;
          paramInt = i;
          if (i5 != arrayOfByte[i4]) {
            break;
          }
          arrayOfByte = this.window;
          i += 1;
          i5 = arrayOfByte[i];
          arrayOfByte = this.window;
          i4 += 1;
          paramInt = i;
          if (i5 != arrayOfByte[i4]) {
            break;
          }
          arrayOfByte = this.window;
          i += 1;
          i5 = arrayOfByte[i];
          arrayOfByte = this.window;
          i4 += 1;
          paramInt = i;
          if (i5 != arrayOfByte[i4]) {
            break;
          }
          arrayOfByte = this.window;
          i += 1;
          i5 = arrayOfByte[i];
          arrayOfByte = this.window;
          i4 += 1;
          paramInt = i;
          if (i5 != arrayOfByte[i4]) {
            break;
          }
          arrayOfByte = this.window;
          i += 1;
          i5 = arrayOfByte[i];
          arrayOfByte = this.window;
          i4 += 1;
          paramInt = i;
          if (i5 != arrayOfByte[i4]) {
            break;
          }
          arrayOfByte = this.window;
          i += 1;
          i5 = arrayOfByte[i];
          arrayOfByte = this.window;
          i4 += 1;
          paramInt = i;
          if (i5 != arrayOfByte[i4]) {
            break;
          }
          paramInt = i4;
          i4 = i;
        } while (i < i11);
        paramInt = i;
        i7 = 258 - (i11 - paramInt);
        i4 = i11 - 258;
        paramInt = i3;
        i = i4;
        i5 = i1;
        i6 = i2;
        if (i7 <= i3) {
          break label309;
        }
        this.match_start = n;
        paramInt = i7;
        i1 = paramInt;
      } while (i7 >= m);
      i6 = this.window[(i4 + paramInt - 1)];
      i5 = this.window[(i4 + paramInt)];
      i = i4;
    }
    label815:
    return this.lookahead;
  }
  
  void pqdownheap(short[] paramArrayOfShort, int paramInt)
  {
    int k = this.heap[paramInt];
    int i = paramInt << 1;
    int j = paramInt;
    for (paramInt = i;; paramInt = i << 1)
    {
      if (paramInt <= this.heap_len)
      {
        i = paramInt;
        if (paramInt < this.heap_len)
        {
          i = paramInt;
          if (smaller(paramArrayOfShort, this.heap[(paramInt + 1)], this.heap[paramInt], this.depth)) {
            i = paramInt + 1;
          }
        }
        if (!smaller(paramArrayOfShort, k, this.heap[i], this.depth)) {}
      }
      else
      {
        this.heap[j] = k;
        return;
      }
      this.heap[j] = this.heap[i];
      j = i;
    }
  }
  
  final void putShortMSB(int paramInt)
  {
    put_byte((byte)(paramInt >> 8));
    put_byte((byte)paramInt);
  }
  
  final void put_byte(byte paramByte)
  {
    byte[] arrayOfByte = this.pending_buf;
    int i = this.pending;
    this.pending = (i + 1);
    arrayOfByte[i] = paramByte;
  }
  
  final void put_byte(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    System.arraycopy(paramArrayOfByte, paramInt1, this.pending_buf, this.pending, paramInt2);
    this.pending += paramInt2;
  }
  
  final void put_short(int paramInt)
  {
    put_byte((byte)paramInt);
    put_byte((byte)(paramInt >>> 8));
  }
  
  void scan_tree(short[] paramArrayOfShort, int paramInt)
  {
    int n = -1;
    int j = paramArrayOfShort[1];
    int i1 = 0;
    int i = 7;
    int k = 4;
    if (j == 0)
    {
      i = 138;
      k = 3;
    }
    paramArrayOfShort[((paramInt + 1) * 2 + 1)] = -1;
    int m = 0;
    if (m <= paramInt)
    {
      int i2 = paramArrayOfShort[((m + 1) * 2 + 1)];
      i1 += 1;
      if ((i1 < i) && (j == i2))
      {
        j = k;
        k = i1;
      }
      for (;;)
      {
        m += 1;
        i1 = k;
        k = j;
        j = i2;
        break;
        short[] arrayOfShort;
        if (i1 < k)
        {
          arrayOfShort = this.bl_tree;
          i = j * 2;
          arrayOfShort[i] = ((short)(arrayOfShort[i] + i1));
        }
        for (;;)
        {
          k = 0;
          n = j;
          if (i2 != 0) {
            break label267;
          }
          i = 138;
          j = 3;
          break;
          if (j != 0)
          {
            if (j != n)
            {
              arrayOfShort = this.bl_tree;
              i = j * 2;
              arrayOfShort[i] = ((short)(arrayOfShort[i] + 1));
            }
            arrayOfShort = this.bl_tree;
            arrayOfShort[32] = ((short)(arrayOfShort[32] + 1));
          }
          else if (i1 <= 10)
          {
            arrayOfShort = this.bl_tree;
            arrayOfShort[34] = ((short)(arrayOfShort[34] + 1));
          }
          else
          {
            arrayOfShort = this.bl_tree;
            arrayOfShort[36] = ((short)(arrayOfShort[36] + 1));
          }
        }
        label267:
        if (j == i2)
        {
          i = 6;
          j = 3;
        }
        else
        {
          i = 7;
          j = 4;
        }
      }
    }
  }
  
  void send_all_trees(int paramInt1, int paramInt2, int paramInt3)
  {
    send_bits(paramInt1 - 257, 5);
    send_bits(paramInt2 - 1, 5);
    send_bits(paramInt3 - 4, 4);
    int i = 0;
    while (i < paramInt3)
    {
      send_bits(this.bl_tree[(Tree.bl_order[i] * 2 + 1)], 3);
      i += 1;
    }
    send_tree(this.dyn_ltree, paramInt1 - 1);
    send_tree(this.dyn_dtree, paramInt2 - 1);
  }
  
  void send_bits(int paramInt1, int paramInt2)
  {
    if (this.bi_valid > 16 - paramInt2)
    {
      this.bi_buf = ((short)(this.bi_buf | paramInt1 << this.bi_valid & 0xFFFF));
      put_short(this.bi_buf);
      this.bi_buf = ((short)(paramInt1 >>> 16 - this.bi_valid));
      this.bi_valid += paramInt2 - 16;
      return;
    }
    this.bi_buf = ((short)(this.bi_buf | paramInt1 << this.bi_valid & 0xFFFF));
    this.bi_valid += paramInt2;
  }
  
  final void send_code(int paramInt, short[] paramArrayOfShort)
  {
    paramInt *= 2;
    send_bits(paramArrayOfShort[paramInt] & 0xFFFF, paramArrayOfShort[(paramInt + 1)] & 0xFFFF);
  }
  
  void send_tree(short[] paramArrayOfShort, int paramInt)
  {
    int n = -1;
    int j = paramArrayOfShort[1];
    int i2 = 0;
    int i = 7;
    int k = 4;
    if (j == 0)
    {
      i = 138;
      k = 3;
    }
    int m = 0;
    int i1 = k;
    k = i2;
    if (m <= paramInt)
    {
      i2 = paramArrayOfShort[((m + 1) * 2 + 1)];
      k += 1;
      if ((k < i) && (j == i2)) {
        j = i1;
      }
      for (;;)
      {
        m += 1;
        i1 = j;
        j = i2;
        break;
        if (k < i1) {
          do
          {
            send_code(j, this.bl_tree);
            i = k - 1;
            k = i;
          } while (i != 0);
        }
        for (;;)
        {
          k = 0;
          n = j;
          if (i2 != 0) {
            break label253;
          }
          i = 138;
          j = 3;
          break;
          if (j != 0)
          {
            i = k;
            if (j != n)
            {
              send_code(j, this.bl_tree);
              i = k - 1;
            }
            send_code(16, this.bl_tree);
            send_bits(i - 3, 2);
          }
          else if (k <= 10)
          {
            send_code(17, this.bl_tree);
            send_bits(k - 3, 3);
          }
          else
          {
            send_code(18, this.bl_tree);
            send_bits(k - 11, 7);
          }
        }
        label253:
        if (j == i2)
        {
          i = 6;
          j = 3;
        }
        else
        {
          i = 7;
          j = 4;
        }
      }
    }
  }
  
  void set_data_type()
  {
    int k = 0;
    int n = 0;
    int i = 0;
    int m;
    int j;
    for (;;)
    {
      m = n;
      j = k;
      if (k >= 7) {
        break;
      }
      i += this.dyn_ltree[(k * 2)];
      k += 1;
    }
    for (;;)
    {
      k = i;
      n = j;
      if (j >= 128) {
        break;
      }
      m += this.dyn_ltree[(j * 2)];
      j += 1;
    }
    while (n < 256)
    {
      k += this.dyn_ltree[(n * 2)];
      n += 1;
    }
    if (k > m >>> 2) {}
    for (i = 0;; i = 1)
    {
      this.data_type = ((byte)i);
      return;
    }
  }
  
  void tr_init()
  {
    this.l_desc.dyn_tree = this.dyn_ltree;
    this.l_desc.stat_desc = StaticTree.static_l_desc;
    this.d_desc.dyn_tree = this.dyn_dtree;
    this.d_desc.stat_desc = StaticTree.static_d_desc;
    this.bl_desc.dyn_tree = this.bl_tree;
    this.bl_desc.stat_desc = StaticTree.static_bl_desc;
    this.bi_buf = 0;
    this.bi_valid = 0;
    this.last_eob_len = 8;
    init_block();
  }
  
  static class Config
  {
    int func;
    int good_length;
    int max_chain;
    int max_lazy;
    int nice_length;
    
    Config(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      this.good_length = paramInt1;
      this.max_lazy = paramInt2;
      this.nice_length = paramInt3;
      this.max_chain = paramInt4;
      this.func = paramInt5;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.Deflate
 * JD-Core Version:    0.7.0.1
 */