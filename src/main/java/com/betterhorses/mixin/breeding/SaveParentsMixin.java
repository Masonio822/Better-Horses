package com.betterhorses.mixin.breeding;

import com.betterhorses.BetterHorses;
import com.betterhorses.duck.TrackedParents;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@SuppressWarnings({"MissingUnique", "AddedMixinMembersNamePattern"})
@Mixin(AbstractHorseEntity.class)
public abstract class SaveParentsMixin extends AnimalEntity implements TrackedParents {

    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<String> PARENTS = DataTracker.registerData(AbstractHorseEntity.class, TrackedDataHandlerRegistry.STRING);

    protected SaveParentsMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("DataFlowIssue")
    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    private void readParents(NbtCompound nbt, CallbackInfo ci) {
        this.dataTracker.set(PARENTS, nbt.contains("Parents") ? nbt.get("Parents").asString() : "");
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    private void writeParents(NbtCompound nbt, CallbackInfo ci) {
        if (this.dataTracker.get(PARENTS) != null || this.dataTracker.get(PARENTS).isEmpty()) {
            nbt.putString("Parents", this.dataTracker.get(PARENTS));
        }
    }

    @Override
    public Optional<NbtCompound> getParentsNbt() {
        try {
            if (this.dataTracker.get(PARENTS).isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(StringNbtReader.parse(this.dataTracker.get(PARENTS)));
        } catch (CommandSyntaxException e) {
            BetterHorses.LOGGER.warn("Could not read parent nbt of entity!");
            return Optional.empty();
        }
    }

    @Override
    public void setParentsNbt(NbtCompound parentsNbt) {
        this.dataTracker.set(PARENTS, parentsNbt.asString());
    }

    @Override
    public void setParents(AbstractHorseEntity horse1, AbstractHorseEntity horse2) {
        NbtCompound nbt = this.writeNbt(new NbtCompound());

        NbtCompound addThis = new NbtCompound();
        addThis.putInt("Variant", horse1.writeNbt(new NbtCompound()).getInt("Variant"));

        NbtCompound thisAttributes = new NbtCompound();
        thisAttributes.putDouble("Health", horse1.getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH));
        thisAttributes.putDouble("Speed", horse1.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
        thisAttributes.putDouble("Jump", horse1.getAttributes().getValue(EntityAttributes.GENERIC_JUMP_STRENGTH));
        addThis.put("Attributes", thisAttributes);

        NbtCompound addOther = new NbtCompound();
        addOther.putInt("Variant", horse2.writeNbt(new NbtCompound()).getInt("Variant"));

        NbtCompound otherAttributes = new NbtCompound();
        otherAttributes.putDouble("Health", horse2.getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH));
        otherAttributes.putDouble("Speed", horse2.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
        otherAttributes.putDouble("Jump", horse2.getAttributes().getValue(EntityAttributes.GENERIC_JUMP_STRENGTH));
        addOther.put("Attributes", otherAttributes);

        NbtList parents = new NbtList();
        parents.add(addThis);
        parents.add(addOther);
        nbt.put("Parents", parents);

        NbtComponent.of(nbt).applyToEntity(this);
        this.setParentsNbt(nbt);
    }

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    private void addParentTracker(DataTracker.Builder builder, CallbackInfo callbackInfo) {
        builder.add(PARENTS, "");
    }
}
