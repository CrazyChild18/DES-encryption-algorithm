package Controller;

import data.Data;

public class DESAlgorithm {
    
	byte[] key;
    
	// ���������ֽ�����
    public DESAlgorithm(String key) {
        this.key = key.getBytes();
    }
    
    private static Data data_parameter = new Data();
    private static final int[] IP = data_parameter.getIP();
    // 64 ���ʼ�û���
    private static final int[] IP1 = data_parameter.getIP1();
    private static final int[] PC1 = data_parameter.getPC1();
    // ѭ����λ����������Կ������
    private static final int[] LeftMove = data_parameter.getLeftMove();
    // 48
    private static final int[] PC2 = data_parameter.getPC2(); 
    // 48
    private static final int[] E = data_parameter.getE(); 
    private static final int[] P = data_parameter.getP();
    //S�У�8��S�任��
    private static final int[][][] SBox = data_parameter.getSBox();
    
    /** 
     * �ӽ��ܵĿ������̲���
     * ���Ĳ���
     * @param inputData: �����������ݵ�byte
     * 		����ʱ����data.getBytes
     * 		����ʱʹ�����ݵ�byte���͵�����
     * @param flag: ��ʾ���ܻ���ܲ���
     */
    public byte[] DesStart(byte[] inputData, int flag) {
    	// ���Ƚ�byte����ÿ8λ���
    	// ͨ����ʽ����֤���ݳ���Ϊ8��������
        byte[] fill_key = FillByteData(key);
        byte[] fill_data = FillByteData(inputData);
        int dataLen = fill_data.length;
        byte[] result_data = new byte[dataLen];
        // �������ݳ��ȿɻ���Ϊ����8λ
        int multiple = dataLen / 8;
        
        // ���� or ��������
        // �����8���ֽ�һ����м��ܣ�����ٺϲ�
        // �õ�result_dataΪ�ܽ��
        for (int i = 0; i < multiple; i++) {
            byte[] tmpkey = new byte[8];
            byte[] tmpdata = new byte[8];
            System.arraycopy(fill_key, 0, tmpkey, 0, 8);
            System.arraycopy(fill_data, i * 8, tmpdata, 0, 8);
            byte[] tmpresult = CodedData(tmpkey, tmpdata, flag);
            System.arraycopy(tmpresult, 0, result_data, i * 8, 8);
        }
        
        // ��Ϊ������ȥ�����λ
        byte[] decrypData = null;
        if (flag == 0) {
        	System.out.print("After: ");
        	for(byte e : result_data) {
        		System.out.print(e + " ");
        	}
        	System.out.println();
        	// ��ð�����λ���ܳ���
            int total_len = dataLen; //16
            int delete_len = result_data[total_len - 8 - 1]; 
            if ((delete_len >= 1) && (delete_len <= 8)) {
            	
            }else {
            	delete_len = 0;
            }
            decrypData = new byte[total_len - delete_len - 8];
            boolean del_flag = true;
            for (int k = 0; k < delete_len; k++) {
                if (delete_len != result_data[total_len - 8 - (k + 1)])
                    del_flag = false;
            }
            if (del_flag == true) {
                System.arraycopy(result_data, 0, decrypData, 0, total_len - delete_len - 8);
            }
        }
        
        // ����������ĺͽ��ܺ����ĵ�ascii����
        // flagΪ1ʱ�������ascii����, Ϊ0ʱ������ܺ�����ascii����
        String bitData = "";
        if (flag == 1){
            for (int i = 0; i < result_data.length; i++) {
            	bitData += String.valueOf(result_data[i]);
            }
            String regEx = "[-]";
            String a = "";
            bitData = bitData.replaceAll(regEx, a);
        }else if (flag == 0){
            for (int i = 0; i < decrypData.length; i++) {
            	bitData += String.valueOf(decrypData[i]);
            }
        }
        if(flag == 1) {
        	return result_data;
        }else {
        	return decrypData;
        }
    }
    
    /**
     * ��������
     * ������des_dataת��Ϊ�������ֽ�����
     * ������des_keyת��Ϊ�������ֽ�����
     * @param des_key �����Ⱥ���Կ
     * @param des_data �����Ⱥ�����
     * @param flag ���ܻ����
     * @return encrypt ����������
     */
    private byte[] CodedData(byte[] des_key, byte[] des_data, int flag) {
    	// ����Կ�ֽ�����ת���ɶ������ֽ�����
        int[] keydata = new int[64];
        keydata = ByteToBirnary(des_key);
        
        // �����������ֽ�����ת���ɶ������ֽ�����
        int[] encryptdata = new int[64];
        encryptdata = ByteToBirnary(des_data);
        
        // ��������Կ
        int[][] sub_key = GetSubKey(keydata);
        
        // ִ�м��ܽ��ܲ���
        int i;
        int flags = flag;
        //��ų�ʼ�任���64λ����
        int[] M = new int[64];
        //������ʼ�任���64λ����
        int[] MIP_1 = new int[64];
        
        for (i = 0; i < 64; i++) {
        	// ����IP��ʼ�任����������=�߼�����-1
            M[i] = encryptdata[IP[i] - 1];
        }
        
        if (flags == 1) { // ����
            for (i = 0; i < 16; i++) {
            	fFunction(M, i, flags, sub_key);
            }
        } else if (flags == 0) { // ����
            for (i = 15; i > -1; i--) {
            	fFunction(M, i, flags, sub_key);
            }
        }

        for (i = 0; i < 64; i++) {
        	// �������ʼ�û�IP-1����
            MIP_1[i] = M[IP1[i] - 1];
        }
        // ��64λ����ת��Ϊ8λ�ֽ�
        byte[] encrypt = BirnaryToByte(MIP_1);
        //���ز�����ɺ��ֽ�����
        return encrypt;
    }
    
    /**
     * ��������Կ
     * @param keyarray: �洢ÿһ�����ɵ���Կ
     * @param key: ��������Կ
     * @return 
     */
    private int[][] GetSubKey(int[] key) {
    	int[][] sub_key = new int[16][48];
        int i, j;
        int[] K0 = new int[56];
        for (i = 0; i < 56; i++) {
        	// ��Կ����PC-1�任
            K0[i] = key[PC1[i] - 1];
        }
        for (i = 0; i < 16; i++) {
        	//���ִζ�����Կ������������
        	LeftMove(K0, LeftMove[i]);
            for (j = 0; j < 48; j++) {
            	//����PC2�任������������Կkeyarray[i][j]
            	sub_key[i][j] = K0[PC2[j] - 1];
            }
        }
        return sub_key;
    }
    
    /**
     * ����Կ���ƣ�����ѭ����λ��
     * @param k: ����Կ
     * @param offset: ƫ����
     */
    private void LeftMove(int[] k, int offset) {
        int i;
        int[] c0 = new int[28];
        int[] d0 = new int[28];
        int[] c1 = new int[28];
        int[] d1 = new int[28];
        //��56λ����ƽ�ֳ�����28λcO��cO
        for (i = 0; i < 28; i++) {
            c0[i] = k[i];
            d0[i] = k[i + 28];
        }
        if (offset == 1) {
        	//ѭ������һλ
            for (i = 0; i < 27; i++) {
                c1[i] = c0[i + 1];
                d1[i] = d0[i + 1];
            }
            c1[27] = c0[0];
            d1[27] = d0[0];
        } else if (offset == 2) {
        	//ѭ��������λ
            for (i = 0; i < 26; i++) {
                c1[i] = c0[i + 2];
                d1[i] = d0[i + 2];
            }
            c1[26] = c0[0];
            d1[26] = d0[0];
            c1[27] = c0[1];
            d1[27] = d0[1];
        }
        //�������������ƺ����������28λ����c1��d1�ϲ�
        for (i = 0; i < 28; i++) {
            k[i] = c1[i];
            k[i + 28] = d1[i];
        }
    }
    
    /**
     * ������ת��Ϊ��������
     * �洢���ֽ�����
     */
    private int[] ByteToBirnary(byte[] intdata) {
        int i;
        int j;
        int[] IntDa = new int[8];
        for (i = 0; i < 8; i++) {
            IntDa[i] = intdata[i];
            if (IntDa[i] < 0) {
                IntDa[i] += 256;
                IntDa[i] %= 256;
            }
        }
        int[] IntVa = new int[64];
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                IntVa[((i * 8) + 7) - j] = IntDa[i] % 2;
                IntDa[i] = IntDa[i] / 2;
            }
        }
        return IntVa;
    }
    
    /** 
     * E����
     * @param R0
     * @param keyarray
     * @param times
     * @return RE ��ž���E�任�������
     * 
     * ����E�任���䣬��32λ��Ϊ48λ
     * 		���Ⱦ���E�任�������������ԿKeyArray[times][i]��λ������λ�ӷ�����
     * 		Ȼ���������
     */
    private int[] E(int[] R0, int[][] keyarray, int times) {
    	int[] RE = new int[48];
        for (int i = 0; i < 48; i++) {
            RE[i] = R0[E[i] - 1];
            RE[i] = RE[i] + keyarray[times][i];
            //�����򣬵�������ȵ������ʱ���Ϊ0ʱ�����ǵ�������Ϊ2��0
            if (RE[i] == 2 || RE[i] == 0) {
                RE[i] = 0;
            }
        }
    	return RE;
    }
    
    /**
     *  S�б任
     *  ��48λ���ݷֳ�8�飬ÿ��6λ
     *  ͨ������ת��Ϊ6bit������
     *  �������
     * @param RE
     * @return ans
     */
    private int[] S(int[] input) {
    	int[][] S = new int[8][6];
    	int[] sBoxData = new int[8];
    	int[] output = new int[32];
    	for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                S[i][j] = input[(i * 6) + j];
            }
            // ���澭��S�У��õ�8����
            sBoxData[i] = SBox[i][(S[i][0] << 1) + S[i][5]][(S[i][1] << 3) + (S[i][2] << 2) + (S[i][3] << 1) + S[i][4]];
            // 8�����任���Ϊ������
            for (int j = 0; j < 4; j++) {
            	output[((i * 4) + 3) - j] = sBoxData[i] % 2;
                sBoxData[i] = sBoxData[i] / 2;
            }
        }
    	return output;
    }
    
    /**
     *  P�任
     *  ��S�������32λ������Ϊ�����������P�任��P[]��ѭ����λ��
     *  	RP[i]��Ÿ���ѭ���任���������ݵĵ�nλ
     *  	������
     *  	������32λ������L��R���ºϳ�M����������M
     *  	���һ�α任ʱ�����Ҳ����л������˴��������α任ʵ�ֲ���
     * @param S S���������
     * @param M ��������
     * @param L0
     * @param L1
     * @param R0
     * @param R1
     * @param flag
     * @param times
     */
    private void P(int[] S, int[] M, int[] L0, int[] L1, int[] R0, int[] R1, int flag, int times) {
    	int[] RP = new int[32];
    	for (int i = 0; i < 32; i++) {
    		// RP[i]��Ÿ���ѭ���任���������ݵĵ�nλ
    		RP[i] = S[P[i] - 1];
            // �ұ��Ƶ����
            L1[i] = R0[i];
            // �������
            R1[i] = L0[i] + RP[i];
            if (R1[i] == 2 || R1[i] == 0) {
                R1[i] = 0;
            }
            // ������32λ������L��R���ºϳ�M����������M
            if (((flag == 0) && (times == 0)) || ((flag == 1) && (times == 15))) {
                M[i] = R1[i];
                M[i + 32] = L1[i];
            }
            else {
                M[i] = L1[i];
                M[i + 32] = R1[i];
            }
        }
	}
    
    /***f����������E�任��������Կ���S��ѹ����P�任***/
    private void fFunction(int[] M, int times, int flag, int[][] keyarray) {
        int[] L0 = new int[32];
        int[] R0 = new int[32];
        int[] L1 = new int[32];
        int[] R1 = new int[32];
        
        //��64λ���ķ�Ϊ����32λ����������
        for (int i = 0; i < 32; i++) {
        	// �������ĳ�ʼ��
            L0[i] = M[i];
            // �����Ҳ�ĳ�ʼ��
            R0[i] = M[i + 32];
        }
        // E�任����
        int[] RE = E(R0, keyarray, times);
        //S�б任
        int[] sValue = S(RE);
        // P�任
        P(sValue, M, L0, L1, R0, R1, flag, times);
    }
    
    /***���洢64λ���������ݵ������е�����ת��Ϊ�˸�������byte��****/
    private byte[] BirnaryToByte(int[] data) {
    	byte[] value = new byte[8];
        int i, j;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                value[i] += (data[(i << 3) + j] << (7 - j));
            }
        }
        for (i = 0; i < 8; i++) {
            value[i] %= 256;
            if (value[i] > 128) {
                value[i] -= 255;
            }
        }
        return value;
    }
    
    /**
     * ��ʽ��������8λ�����ֽڵĲ���
     * ���ȱ����λ��8��������
     * ����λ��ʹ��ȱʧ������byte
     */
    private byte[] FillByteData(byte[] data) {
        int len = data.length;
        int need = 8 - (len % 8);
        byte[] fillData = new byte[len + need];
        System.arraycopy(data, 0, fillData, 0, len);
        for (int i = len; i < len + need; i++)
        	fillData[i] = (byte) need;
        System.out.print("Before: ");
        for (byte e : fillData) {
        	System.out.print(e + " ");
        }
        System.out.println();
        return fillData;
    }

    
}