//============================================================================
// Name        : TestCPP.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C, Ansi-style
//============================================================================

#include <stdio.h>
#include <string.h>
#include <string.h>
#include <iostream>
#include <stdlib.h>
#include <cstdlib>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <time.h>

#define VMSTAT_FILE "/proc/vmstat"

typedef struct vmStatStruct {
	const char *statName; /* VM stat name */
	unsigned long *value; /* stat value in return struct */
} vmStatStruct;

static int compare_vmstats_func(const void *a, const void *b) {
	return strcmp(((const vmStatStruct*) a)->statName,
			((const vmStatStruct*) b)->statName);
}

timespec diff(timespec start, timespec end);
void vmstat(void);

int main(int argc, char* argv[]) {
	long FILESIZE = 100 * 1 << 20;
	if (argc >= 2) {
		FILESIZE = atol(argv[1]) * 1 << 20;
	}

	bool nvm = 1;

	if (argc >= 3) {
		nvm = atoi(argv[2]);
	}

	int nBuffers = 10;

	if (argc >= 4) {
		nBuffers = atoi(argv[3]);
	}

	//mapped size is multiple of pagesize.
	// offset has to be at page boundary
	long pagesize = sysconf(_SC_PAGESIZE);
	int nPages = FILESIZE / pagesize / nBuffers;
	std::cout << " Buffersize in nPages:" << nPages << std::endl;
	long bufferSize = nPages * pagesize;
	std::cout << " Buffersize in bytes:" << bufferSize << std::endl;

	bool fsync_arg = 1;
	if (argc >= 5) {
		fsync_arg = atoi(argv[4]);
	}

	bool msync_arg = 0;
	if (argc >= 6) {
		msync_arg = atoi(argv[5]);
	}

	int commitByteInterval = 5 * (1 << 20);
	if (argc >= 7) {
		commitByteInterval = atoi(argv[6]) * 1 << 20;
	}

	bool preallocate = true;
	if (argc >= 8) {
			preallocate = atoi(argv[7]);
		}

	std::cout << "\n args: filesize:" << FILESIZE << "  nvm:" << nvm
			<< " nBuffers:" << nBuffers << " fsync:" << fsync_arg << " msync:"
			<< msync_arg << " preallocate:" <<preallocate<<"  commitByteInterval:" << commitByteInterval
			<< std::endl;

	/* Open a file for writing.
	 *  - Creating the file if it doesn't exist.
	 *  - Truncating it to 0 size if it already exists. (not really needed)
	 *
	 * Note: "O_WRONLY" mode is not sufficient when mmaping.
	 */

	timespec start_time;
	timespec end_time;
	struct timespec spec;

	const char *filepath;
	if (!nvm) {
		filepath = "/home/adi/mmap_exps/c_file1";
	} else {
		filepath = "/home/adi/pmem/c_nvm_file1";
	}

	int fd = open(filepath, O_RDWR | O_CREAT | O_TRUNC, (mode_t) 0600);

	if (fd == -1) {
		perror("Error opening file for writing");
		exit(EXIT_FAILURE);
	}

	if (lseek(fd, FILESIZE - 1, SEEK_SET) == -1) {
		close(fd);
		perror("Error calling lseek() to 'stretch' the file");
		exit(EXIT_FAILURE);
	}

	/* Something needs to be written at the end of the file to
	 * have the file actually have the new size.
	 * Just writing an empty string at the current file position will do.
	 *
	 * Note:
	 *  - The current position in the file is at the end of the stretched
	 *    file due to the call to lseek().
	 *  - An empty string is actually a single '\0' character, so a zero-byte
	 *    will be written at the last byte of the file.
	 */

	if (write(fd, "", 1) == -1) {
		close(fd);
		perror("Error writing last byte of the file");
		exit(EXIT_FAILURE);
	}

	if(preallocate) {
		
          int pafd = open(filepath, O_RDWR | O_CREAT | O_TRUNC, (mode_t) 0600);

        if (pafd == -1) {
                perror("Error opening file for writing");
                exit(EXIT_FAILURE);
        }

        if (lseek(pafd, FILESIZE - 1, SEEK_SET) == -1) {
                close(pafd);
                perror("Error calling lseek() to 'stretch' the file");
                exit(EXIT_FAILURE);
        }


        if (write(pafd, "", 1) == -1) {
                close(pafd);
                perror("Error writing last byte of the file");
                exit(EXIT_FAILURE);
        }


               // mmap the whole file.
		char *pre_map = (char *) mmap(0, nPages * pagesize *nBuffers, PROT_READ | PROT_WRITE,
				MAP_SHARED | MAP_POPULATE, pafd, 0);
        if(pre_map == MAP_FAILED) {
        	close(pafd);
        				perror("Error mmapping the file");
        				exit(EXIT_FAILURE);

        }
		for(int k=0; k<nPages*pagesize*nBuffers ; k ++) {
			pre_map[k] = '\0';
		}

		//free(pre_map);

		if (munmap(pre_map, nPages * pagesize *nBuffers) == -1) {
					perror("Error un-mmapping the file in preallocate phase");
					/* Decide here whether to close(fd) and exit() or not. Depends... */
				}

		close(pafd);


	}


	// Now the file is ready to be mmapped.
	//printf("\n Relevant VM Stats Before allocating mmaps[][]");
	//vmstat();

	char** mmaps = (char**) malloc(nBuffers * sizeof(char*));
	if (mmaps == NULL) {
		perror("could not allocate mmaps char*[]");
		exit(EXIT_FAILURE);
	}

	long offset = 0;

	char* dataBuffer = (char*) malloc(bufferSize * sizeof(char));
	for (int i = 0; i < bufferSize; ++i) {
		dataBuffer[i] = 'D';
	}

	printf("\n Relevant VM Stats Before mmap");
	vmstat();

	printf("\n pagesize is: %ld \n", pagesize);
	clock_gettime(CLOCK_REALTIME, &spec);
	start_time = spec;

	for (int i = 0; i < nBuffers; i++) {
		char *map = (char *) mmap(0, nPages * pagesize, PROT_READ | PROT_WRITE,
		MAP_SHARED | MAP_POPULATE, fd, offset);
		printf("mapped at address: %p", map);
		if (map == MAP_FAILED) {
			close(fd);
			perror("Error mmapping the file");
			exit(EXIT_FAILURE);
		}
		mmaps[i] = map;
		offset += nPages * pagesize;
		printf(" offset=%ld", offset);

	}

	clock_gettime(CLOCK_REALTIME, &spec);
	end_time = spec;

	printf("\n\n **** Mapping time:%ld microseconds ****",
			(diff(start_time, end_time).tv_nsec) / 1000);
	printf("\n Relevant VM Stats After mmap");
	vmstat();

	clock_gettime(CLOCK_REALTIME, &spec);
	start_time = spec;
	/* Now write chars to the file as if it were memory (an array of char).
	 */
	/*for (int i = 0; i < bufferSize; ++i) {
	 for (int j = 0; j < nBuffers; j++) {
	 mmaps[j][i] = 'D' + j;
	 }
	 } */

	for (int j = 0; j < nBuffers; j++) {
		memcpy(mmaps[j], dataBuffer, bufferSize);
	}

	clock_gettime(CLOCK_REALTIME, &spec);
	end_time = spec;



	printf("\n\n **** MappingPUT time:%ld microseconds ****",
			(diff(start_time, end_time).tv_nsec) / 1000);

	printf("\n Relevant VM Stats after Map Put");
		vmstat();

	/* Pages are faulted in now. Let's run it once more.
	 * This should be faster now.
	 */

	// Change data content.
		for (int i = 0; i < bufferSize; ++i) {
				dataBuffer[i] = 'Z';
			}
	clock_gettime(CLOCK_REALTIME, &spec);
		start_time = spec;
		/* Now write chars to the file as if it were memory (an array of char).
		 */
		/*for (int i = 0; i < bufferSize; ++i) {
		 for (int j = 0; j < nBuffers; j++) {
		 mmaps[j][i] = 'D' + j;
		 }
		 } */

		for (int j = 0; j < nBuffers; j++) {
			memcpy(mmaps[j], dataBuffer, bufferSize);
		}

		clock_gettime(CLOCK_REALTIME, &spec);
		end_time = spec;



		printf("\n\n **** AFTER FAULTS MappingPUT time:%ld microseconds ****",
				(diff(start_time, end_time).tv_nsec) / 1000);

	printf("\n Relevant VM Stats after second map put and before fsync/msync");
	vmstat();

	clock_gettime(CLOCK_REALTIME, &spec);
	start_time = spec;

	// Write it now to disk
	if (msync_arg) {
		for (int j = 0; j < nBuffers; j++) {
			if (msync(mmaps[j], bufferSize, MS_SYNC) == -1) {
				perror("Could not sync the file to disk");
			}
		}
	}

	if (fsync_arg) {
		fsync(fd);
	}

	clock_gettime(CLOCK_REALTIME, &spec);
	end_time = spec;

	if (msync_arg) {
		printf("\n\n **** msync time:%ld microseconds ****",
				(diff(start_time, end_time).tv_nsec) / 1000);
	}
	if (fsync_arg) {
		printf("\n\n **** fsync time:%ld microseconds ****",
				(diff(start_time, end_time).tv_nsec) / 1000);
	}

	printf("\n Relevant VM Stats After fsync/msync");
	vmstat();
	/* Don't forget to free the mmapped memory
	 */
	clock_gettime(CLOCK_REALTIME, &spec);
	start_time = spec;

	for (int j = 0; j < nBuffers; j++) {
		if (munmap(mmaps[j], bufferSize) == -1) {
			perror("Error un-mmapping the file");
			/* Decide here whether to close(fd) and exit() or not. Depends... */
		}
	}

	clock_gettime(CLOCK_REALTIME, &spec);
	end_time = spec;

	printf("\n\n **** UnMapping time:%ld microseconds **** \n",
			(diff(start_time, end_time).tv_nsec) / 1000);

	/* Un-mmaping doesn't close the file, so we still need to do that.
	 */

	//printf("\n Relevant VM Stats After unmapping");
	//vmstat();
	close(fd);
	return 0;

}

timespec diff(timespec start, timespec end) {
	timespec temp;

	if ((end.tv_nsec - start.tv_nsec) < 0) {
		temp.tv_sec = end.tv_sec - start.tv_sec - 1;
		temp.tv_nsec = 1000000000 + end.tv_nsec - start.tv_nsec;
	} else {
		temp.tv_sec = end.tv_sec - start.tv_sec;
		temp.tv_nsec = end.tv_nsec - start.tv_nsec;
	}
	return temp;
}

void vmstat(void) {
	unsigned long vm_nr_dirty;           // dirty writable pages
	unsigned long vm_nr_writeback;       // pages under writeback
	unsigned long vm_nr_dirtied;
	unsigned long vm_nr_written;
	unsigned long vm_nr_dirty_threshold;
	unsigned long vm_nr_file_pages;
	unsigned long vm_nr_mapped;
	unsigned long vm_nr_pgmajfault;

	//Make sure the names are in sorted order as bsearch is used.
	static const vmStatStruct dirty_page_stats[] = { { "nr_dirtied",
			&vm_nr_dirtied }, // page version of meminfo Dirty
			{ "nr_dirty", &vm_nr_dirty }, { "nr_dirty_threshold",
					&vm_nr_dirty_threshold }, { "nr_file_pages",
					&vm_nr_file_pages }, { "nr_mapped", &vm_nr_mapped },
					{ "nr_writeback",&vm_nr_writeback },
					{ "nr_written", &vm_nr_written },
					{ "pgmajfault", &vm_nr_pgmajfault }

	};

	static char buf[2048];
	int fd = -1;
	int readBytes = -1;
	if ((fd = open(VMSTAT_FILE, O_RDONLY)) == -1) {
		fputs("Could not open /proc/vmstat", stderr);
		fflush(NULL);
		_exit(102);

	}
	lseek(fd, 0L, SEEK_SET);
	if ((readBytes = read(fd, buf, sizeof buf - 1)) < 0) {
		fputs("Could not read /proc/vmstat", stderr);
		fflush(NULL);
		_exit(103);
	}
	buf[readBytes] = '\0';

	char statNameBuf[32]; /* big enough to hold any stat name */
	vmStatStruct dirtyStat = { statNameBuf, NULL };
	vmStatStruct *foundStat;
	char *word;
	char *delimiter;

	word = buf;

	while (1) {
		delimiter = strchr(word, ' ');
		if (!delimiter)
			break;
		*delimiter = '\0';

		strcpy(statNameBuf, word);

		foundStat = (vmStatStruct*) bsearch(&dirtyStat, dirty_page_stats, 8,
				sizeof(vmStatStruct), compare_vmstats_func);
		word = delimiter + 1;

		if (foundStat) {

			*(foundStat->value) = strtoul(word, &delimiter, 10);
			fprintf(stdout, "\n %s=%lu", foundStat->statName,
					*(foundStat->value));
			delimiter = strchr(word, '\n');
			if (!delimiter)
				break;
			word = delimiter + 1;
		} else {
			//Go to next stat. next line.
			delimiter = strchr(word, '\n');
			if (!delimiter)
				break;
			word = delimiter + 1;
		}

	}

}

