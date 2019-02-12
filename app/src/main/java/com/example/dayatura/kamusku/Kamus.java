package com.example.dayatura.kamusku;


import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Kamus {

    public static final int HEADER = 16;
    public static final int ROOT = 0;
    public static final int CURR = 4;
    public static final int SIZE = 8;
    public static final int ADDRESS_SIZE = 12;

    public static final int NULL_INDEX = 0;

    private byte[] tree;

    public Kamus(int address_size) {
        tree = new byte[10 * (int) Math.pow(256, 3) - 1];
        this.set_root(0);
        this.set_curr(HEADER);
//        this.set_size((int) Math.pow(256, address_size) - 1);
        this.set_size(10 * (int) Math.pow(256, 3));
        this.set_address_size(address_size);
    }


    //region ===== setter getter Header =====

    private void set_root(int root) {
        byte bRoot[] = ByteBuffer.allocate(4).putInt(root).array();
        System.arraycopy(bRoot, 0, this.tree, ROOT, bRoot.length);
    }

    int get_root() {
        byte bRoot[] = new byte[4];
        System.arraycopy(this.tree, ROOT, bRoot, 0, bRoot.length);
        return ByteBuffer.wrap(bRoot).getInt();
    }

    private void set_curr(int curr) {
        byte bCurr[] = ByteBuffer.allocate(4).putInt(curr).array();
        System.arraycopy(bCurr, 0, this.tree, CURR, bCurr.length);
    }

    int get_curr() {
        byte bCurr[] = new byte[4];
        System.arraycopy(this.tree, CURR, bCurr, 0, bCurr.length);
        return ByteBuffer.wrap(bCurr).getInt();
    }

    private void set_size(int size) {
        byte bSize[] = ByteBuffer.allocate(4).putInt(size).array();
        System.arraycopy(bSize, 0, this.tree, SIZE, bSize.length);
    }

    int get_size() {
        byte bSize[] = new byte[4];
        System.arraycopy(this.tree, SIZE, bSize, 0, bSize.length);
        return ByteBuffer.wrap(bSize).getInt();
    }

    private void set_address_size(int address_size) {
        byte bAddressSize[] = ByteBuffer.allocate(4).putInt(address_size).array();
        System.arraycopy(bAddressSize, 0, this.tree, ADDRESS_SIZE, bAddressSize.length);
    }

    int get_address_size() {
        byte bAddressSize[] = new byte[4];
        System.arraycopy(this.tree, ADDRESS_SIZE, bAddressSize, 0, bAddressSize.length);
        return ByteBuffer.wrap(bAddressSize).getInt();
    }

    //endregion

    //region ===== addres getter =====

    private int get_left_flag(){
        return 2;
    }
    private int get_right_flag(){
        return this.get_left_flag() + this.get_address_size();
    }
    private int get_end_flag(){
        return this.get_right_flag() + this.get_address_size();
    }
    private int get_block_size(){
        return this.get_end_flag() + 4;
    }

    //endregion

    //region ===== setter getter Node ======

    char get_value_node(int n_idx){
        byte value_idx[] = new byte[2];
        System.arraycopy(this.tree, n_idx, value_idx, 0, value_idx.length);
        return ByteBuffer.wrap(value_idx).getChar();
    }
    int get_mark_node(int n_idx){
        byte mark_idx[] = new byte[4];
        System.arraycopy(this.tree, n_idx+get_end_flag(), mark_idx, 0, mark_idx.length);
        return ByteBuffer.wrap(mark_idx).getInt();

    }

    int get_left_node(int n_idx)
    {
        byte l_idx[] = new byte[get_address_size()];
        System.arraycopy(this.tree, n_idx+ get_left_flag(), l_idx, 0, l_idx.length);
        return ByteBuffer.wrap(l_idx).getInt();

    }

    int get_right_node(int n_idx)
    {
        byte r_idx[] = new byte[get_address_size()];
        System.arraycopy(this.tree, n_idx + get_right_flag(), r_idx, 0, r_idx.length);
        return ByteBuffer.wrap(r_idx).getInt();
    }

    void set_value_node(int n_idx, char c)
    {
        byte buffer[] = ByteBuffer.allocate(2).putChar(c).array();
        System.arraycopy(buffer, 0, this.tree, n_idx, buffer.length);
    }

    void set_mark_node(int n_idx, int i)
    {
        byte buffer[] = ByteBuffer.allocate(4).putInt(i).array();

//        if(i!=NULL_INDEX)
            System.arraycopy(buffer, 0, this.tree, n_idx + get_end_flag(), buffer.length);
//        else{
//            buffer = ByteBuffer.allocate(4).putInt(NULL_INDEX).array();
//            System.arraycopy(buffer, 0, this.tree, n_idx + get_end_flag(), buffer.length);
//        }
    }

    void set_left_node(int n_idx, int l_idx)
    {
        byte buffer[] = ByteBuffer.allocate(get_address_size()).putInt(l_idx).array();
        System.arraycopy(buffer, 0, this.tree, n_idx+get_left_flag(), buffer.length);
    }

    void set_right_node( int n_idx,  int r_idx)
    {
        byte buffer[] = ByteBuffer.allocate(get_address_size()).putInt(r_idx).array();
        System.arraycopy(buffer, 0, this.tree, n_idx+get_right_flag(), buffer.length);
    }
    //endregion

    //region ===== tree function =====

    boolean is_empty_tree(){
        return this.get_curr() == HEADER;
    }

    boolean is_full_tree(){
        return !(this.get_curr() < this.get_size());
    }

    int add_node(char[] s, int dict_idx, int s_idx, int s_length)
    {

        if (!is_full_tree())
            if (s_idx < s_length)
            {
                int n_idx = get_curr(), l_idx;

                set_curr(get_curr() + get_block_size());


                set_value_node(n_idx, s[s_idx]);

                //mark
                if (s_length == s_idx + 1)  set_mark_node(n_idx, dict_idx);
                else set_mark_node(n_idx,NULL_INDEX);

                l_idx = add_node(s, dict_idx, s_idx + 1, s_length);
                set_left_node(n_idx, l_idx);

                set_right_node(n_idx, NULL_INDEX);

                return n_idx;
            }
            else
            {
                return NULL_INDEX;
            }
        else
        {
            Log.d(getClass().getName(), String.format("Memori penuh pada input kata %s \n", s.toString()));
            return get_curr();
        }

    }

    //endregion

    //region ===== helper function =====

    int search_word(char[] s,  int s_idx,  int s_length,  int n_idx)
    {
        char c = get_value_node( n_idx);
        if (s_length - s_idx == 1)
        {
            if (c == s[s_idx]){
                return get_mark_node( n_idx);
            }else{
                 int r_idx = get_right_node( n_idx);
                return r_idx == NULL_INDEX ? 0 : search_word( s, s_idx, s_length, r_idx);
            }
        }
        else
        {
             int l_idx, r_idx;

            if (n_idx == NULL_INDEX || s[s_idx] < c)
            {
                return 0;
            }
            else if (s[s_idx] > c)
            {
                r_idx = get_right_node( n_idx);
                return r_idx == NULL_INDEX ? 0 : search_word( s, s_idx, s_length, r_idx);
            }
            else
            {
                l_idx = get_left_node( n_idx);
                return l_idx == NULL_INDEX ? 0 : search_word( s, s_idx + 1, s_length, l_idx);
            }
        }
    }

     int insert_word( char[] s, int dict_idx,  int s_idx,  int s_length,  int n_idx)
    {
        if (s_idx < s_length)
        {
            char c = get_value_node( n_idx);
            int l_idx, r_idx, t_idx;

            if (n_idx == NULL_INDEX || s[s_idx] < c)
            {
                t_idx = add_node( s, dict_idx, s_idx, s_length);
                set_right_node( t_idx, n_idx);

                return t_idx;
            }
            else if (s[s_idx] > c)
            {
                r_idx = get_right_node( n_idx);
                t_idx = r_idx == NULL_INDEX ? add_node( s,dict_idx, s_idx, s_length) : insert_word( s,dict_idx, s_idx, s_length, r_idx);
                set_right_node( n_idx, t_idx);

                return n_idx;
            }
            else
            {
                l_idx = get_left_node( n_idx);
                t_idx = l_idx == NULL_INDEX ? add_node( s,dict_idx, s_idx + 1, s_length) : insert_word( s,dict_idx, s_idx + 1, s_length, l_idx);
                set_left_node( n_idx, t_idx);

                if (s_length == s_idx + 1)
                {
                    set_mark_node( n_idx, dict_idx);
                }

                return n_idx;
            }
        }
        else
        {
            return n_idx;
        }
    }

    //endregion

    // ==== Main Function ====

    boolean search_tree(String text)
    {
        char[] s = text.toCharArray();
        return search_word( s, 0, s.length, get_root()) != 0;
    }

    int dict_idx (String text)
    {
        char[] s = text.toCharArray();
        return search_word( s, 0, s.length, get_root());
    }

    void insert_tree(String text, int dict_idx)
    {
        // if ( is_full_tree(t) ) {
        //     printf("Memori sudah penuh \n");
        // }else
        // {
        char[] s = text.toCharArray();
        int root_idx = get_root();
        root_idx = insert_word( s,dict_idx, 0, s.length, root_idx);
        set_root( root_idx);
        // }

    }


//    load tree here
//    char[] load_tree(char []filename)
//    {
//        t = malloc(sizeof(char) * HEADER );
//
//        unsigned int curr;
//        FILE *f = fopen(filename, "rb");
//
//        fread(t, sizeof(char), HEADER, f);
//        fseek(f, 0, SEEK_SET);
//
//        char *res = realloc(t, sizeof(char) * get_size_max(t) );
//        // free(t);
//
//        fread(res, sizeof(char), get_curr_node(t), f);
//        fclose(f);
//
//        return res;
//    }

    // save tree here
//    void save_tree(char *t, char *filename)
//    {
//        FILE *f;
//
//        f = fopen(filename, "wb");
//        fwrite(t, sizeof(char), get_curr_node(t), f);
//        fclose(f);
//    }

    String print_memory_tree()
    {
        int l_idx, r_idx;

        String result = "";

        result += "*---------------------------*\n";
        result += "| index |        ISI        |\n";
        result += "|---------------------------|\n";

        result += String.format("| 00000 |       %05d       | <-- Root\n", get_root());
        result += String.format("| 00004 |       %05d       | <-- Curr\n", get_curr());
        result += String.format("| 00008 |       %05d       | <-- SizeMax\n", get_size());
        result += String.format("| 00012 |       %d Byte      | <-- Address Size\n", get_address_size());


        for ( int i = HEADER; i < get_curr(); i += get_block_size())
        {
            result += String.format("| %05d |", i);
            result += String.format(" %c |", get_value_node( i));

            l_idx = get_left_node(i);
            if (l_idx != NULL_INDEX)
            {
                result += String.format(" %05d |", l_idx);
            }
            else
            {
                result += String.format("       |");
            }

            r_idx = get_right_node( i);
            if (r_idx != NULL_INDEX)
            {
                result += String.format(" %05d |", r_idx);
            }
            else
            {
                result += String.format("       |");
            }

            result += String.format(" %d\n", get_mark_node(i));
        }

        return result;
    }
//
//    void print_tree(char *t, unsigned int idx, unsigned int deep)
//    {
//        printf("%c", get_value_node(t, idx));
//
//        unsigned int l_idx = get_left_node(t, idx);
//        if (l_idx != NULL_INDEX)
//        {
//            deep++;
//            printf("-");
//            print_tree(t, l_idx, deep);
//            deep--;
//        }
//
//        unsigned int r_idx = get_right_node(t, idx);
//        if (r_idx != NULL_INDEX)
//        {
//            printf("\n");
//            // printf("%d", deep);
//            if (deep != 0)
//                for (unsigned int i = 0; i < (deep); i++)
//            {
//                printf("  ");
//            }
//
//            printf("|\n");
//
//            if (deep != 0)
//                for (unsigned int i = 0; i < deep; i++)
//            {
//                printf("  ");
//            }
//            print_tree(t, r_idx, deep);
//        }
//    }

//    char* convert_tree(char *t, unsigned int addres_size)
//    {
//        char *conv_tree = malloc(sizeof(char) * HEADER );
//
//        // memcpy(&conv_tree, &t, HEADER);
//
//        set_address_size(conv_tree, addres_size);
//        set_root_node(conv_tree, (get_block_size(conv_tree) * (get_root_node(t) - HEADER) / get_block_size(t)) + HEADER );
//        set_curr_node(conv_tree, (get_block_size(conv_tree) * (get_curr_node(t) - HEADER) / get_block_size(t)) + HEADER );
//        set_size_max(conv_tree, (unsigned int) pow(256, addres_size) - 1 );
//
//        char *res = realloc(conv_tree, sizeof(char) * get_size_max(conv_tree) );
//
//        for (unsigned int i = HEADER; i < get_curr_node(t); i += get_block_size(t))
//        {
//            int new_index = (get_block_size(conv_tree) * (i - HEADER) / get_block_size(t)) + HEADER;
//            int new_left  = get_left_node(t,i)==0 ? 0 : (get_block_size(conv_tree) * (get_left_node(t, i) - HEADER) / get_block_size(t)) + HEADER;
//            int new_right = get_right_node(t,i)==0 ? 0 : (get_block_size(conv_tree) * (get_right_node(t, i) - HEADER) / get_block_size(t)) + HEADER;
//
//            set_value_node(res, new_index, get_value_node(t, i));
//            set_left_node(res, new_index, new_left);
//            set_right_node(res, new_index, new_right);
//            set_mark_node(res, new_index, get_mark_node(t,i));
//        }
//
//        return res;
//    }
//
//    unsigned int get_line_number(char *filename){
//        FILE *fileptr;
//        int count_lines = 0;
//        char chr;
//
//        fileptr = fopen(filename, "r");
//        //extract character from file and store in chr
//        chr = getc(fileptr);
//        while (chr != EOF)
//        {
//            //Count whenever new line is encountered
//            if (chr == '\n')
//            {
//                count_lines = count_lines + 1;
//            }
//            //take next character from file.
//            chr = getc(fileptr);
//        }
//        fclose(fileptr);
//
//        return count_lines;
//    }
//
//    typedef char *string;
//
//    char* String_Lower(char *kata)
//    {
//        int i = 0;
//        while (kata[i] != '\0')
//        {
//            if (kata[i] >= 'A' && kata[i] <= 'Z') {
//                kata[i] = kata[i] + 32;
//            }
//            i++;
//        }
//        return kata;
//    }
//
//    void insert_tree_by_file(char* t, char* filename){
//        unsigned int num_of_line = get_line_number(filename);
//
//        unsigned int i;
//        string array[num_of_line];
//        int cur_size = 0;
//
//        FILE *my;
//        my = fopen(filename,"r");
//        for(i = 0; i < num_of_line; i++){
//            fscanf(my, "%*s%n", &cur_size);
//            array[i] = malloc((cur_size+1)*sizeof(*array[i]));
//        }
//        fclose(my);
//
//        my = fopen(filename,"r");
//        for(i = 0; i < num_of_line; i++){
//            fscanf(my, "%s", array[i]);
//            // printf("%s\n", String_Lower(array[i]));
//            insert_tree(t, String_Lower(array[i]));
//        }
//        fclose(my);
//
//        // and when done:
//        for(i = 0; i < num_of_line; i++){
//            free(array[i]);
//        }
//
//    }



}