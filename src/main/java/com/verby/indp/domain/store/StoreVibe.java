package com.verby.indp.domain.store;

import com.verby.indp.domain.store.vo.Vibe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "store_vibe")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreVibe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_vibe_id")
    private Long storeVibeId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(name = "vibe")
    private Vibe vibe;

    public StoreVibe(Store store, Vibe vibe) {
        this.store = store;
        this.vibe = vibe;
    }
}
