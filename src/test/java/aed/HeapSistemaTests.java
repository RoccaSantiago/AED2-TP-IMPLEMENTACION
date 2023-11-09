package aed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class HeapSistemaTests {

@Test
void dHondt() { // No sirve muy bien este test pero no lo borren

    int[] a0 = new int[]{150000, 60000, 160000, 280000, 340000};
    int[] ai0 = new int[]{1, 2, 3, 4, 5};

    int[] a1 = new int[]{340000, 280000, 160000, 60000, 15000};
    int[] ai1 = new int[]{5, 4, 3, 2, 1};

    int[] a2 = new int[]{280000, 170000, 160000, 60000, 15000};
    int[] ai2 = new int[]{4, 5, 3, 2, 1};

    int[] a3 = new int[]{170000, 160000, 140000, 60000, 15000};
    int[] ai3 = new int[]{5, 3, 4, 2, 1};

    int[] a4 = new int[]{160000, 140000, 113333, 60000, 15000};
    int[] ai4 = new int[]{3, 4, 5, 2, 1};

    int[] a5 = new int[]{140000, 113333, 80000, 60000, 15000};
    int[] ai5 = new int[]{4, 5, 3, 2, 1};

    int[] a6 = new int[]{113333, 93333, 80000, 60000, 15000};
    int[] ai6 = new int[]{5, 4, 3, 2, 1};

    int[] a7 = new int[]{93333, 85000, 80000, 60000, 15000};
    int[] ai7 = new int[]{4, 5, 3, 2, 1};

    HeapSistema heap = new HeapSistema(a0);
    // assertEquals(heap.heap(), a1);
}

}