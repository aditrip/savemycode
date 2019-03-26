/*
 * Author: Lauren Foutz
 * This program demonstrates a bug in extending an mmapped file in WSL, 
 * where the mappings to the extended part end up effecting mappings to
 * the beginning of the file.
 *
 * Output Should Be:
 *   Fill the first 64K bytes with 'A'.
 *   Extend the file to 1MB
 *   Now fill the extended part of the file with 'B'.
 *   First byte of the extended part of the file at address 0x7f03a50d6000, should be B: B
 *   First byte of the file at mapped address 0x7f03a50c6000, should be A: A
 *   Address 0x7f03a50d6000 is now: C, address 0x7f03a50c6000 should still be A, is now: A 
 *   Reopen the file and read the first 5 bytes.
 *   First five bytes of the file should be 'AAAAA': AAAAA
 *
 * On WSL the output is:
 *   Fill the first 64K bytes with 'A'.
 *   Extend the file to 1MB
 *   Now fill the extended part of the file with 'B'.
 *   First byte of the extended part of the file at address 0x7fe1db720000, should be B: B
 *   First byte of the file at mapped address 0x7fe1db710000, should be A: B
 *   Address 0x7fe1db720000 is now: C, address 0x7fe1db710000 should still be A, is now: C
 *   Reopen the file and read the first 5 bytes.
 *   First five bytes of the file should be 'AAAAA': CBBBB
 */

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>

#include <errno.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>

int
main(argc, argv)
	int argc;
	char *argv[];
{
	    const char *io_file = "tmp_mmap4_f";
	    int fid = 0, i = 0;
	    void *addr1 = NULL;
	    int open_flags = 0, mode = 0, total_size = 0;
	    char buf[256], *caddr;

	    unlink(io_file);

	    open_flags = O_CREAT | O_TRUNC | O_RDWR;
	    mode = S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH;

	    /* Open a file. */
	    fid = open(io_file, open_flags, mode);

	    total_size = 1024 * 1024;

	    /* Fill the first 64K bytes with 'A'*/
	    printf("Fill the first 64K bytes with 'A'.\n");
	    memset(buf, (int)'A', sizeof(buf));
	    for (i = 0; i < 256; i++) {
		lseek(fid, i * 256, SEEK_SET);
		write(fid, buf, sizeof(buf));
	    }

	    /* mmap the file */
	    addr1 = mmap(NULL, total_size, PROT_READ | PROT_WRITE, MAP_SHARED, fid, 0);

	    /* Extend the file, write the last byte */
	    printf("Extend the file to 1MB\n");
	    lseek(fid, total_size - 1, SEEK_SET);
	    write(fid, buf, sizeof(buf[0]));

	    printf("Now fill the extended part of the file with 'B'.\n");
	    caddr = (char *)(addr1);
	    for (i = 256 * 256; i < total_size; i++) {
		caddr[i] = 'B';
	    }   
	    caddr = caddr + (256 * 256);
	    printf("First byte of the extended part of the file at address %p, should be B: %c\n", caddr,  caddr[0]);
	    printf("First byte of the file at mapped address %p, should be A: %c\n", addr1, *(char *)addr1);
	    caddr[0] = 'C';
	    printf("Address %p is now: %c, address %p should still be A, is now: %c \n", caddr, caddr[0], addr1, *(char *)addr1);

	    munmap(addr1, total_size);
	    fsync(fid);
	    close(fid);

	    /* Reopen the file and read the first 5 bytes. */
	    printf("Reopen the file and read the first 5 bytes.\n");
	    fid = open(io_file, O_RDWR, mode);
	    read(fid, buf, sizeof(buf[0]) * 5);
	    close(fid);
	    buf[5] = '\0';
	    printf("First five bytes of the file should be 'AAAAA': %s\n", buf);

	    return 0;
}
