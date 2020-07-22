package red.jad.ladderwarp;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(at = @At("HEAD"), method = "onUse")
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if(!world.isClient()){
            if(player.getMainHandStack().isEmpty() && !player.isSneaking()){
                if(state.isIn(BlockTags.CLIMBABLE)){
                    int up = 0, down = 0;
                    while(world.getBlockState(pos.add(0, up + 1, 0)).getBlock().isIn(BlockTags.CLIMBABLE)) up++;
                    while(world.getBlockState(pos.add(0, down - 1, 0)).getBlock().isIn(BlockTags.CLIMBABLE)) down--;
                    if(up + down != 0 && Math.abs(up + down) != 1){
                        up--; // to not suffocate
                        double newY = pos.getY() + (up > Math.abs(down) ? up : down);
                        if(world.getBlockState(player.getBlockPos()).getBlock().isIn(BlockTags.CLIMBABLE)){
                            if(pos.getX() == Math.floor(player.getX()) && pos.getZ() == Math.floor(player.getZ())){
                                if(!player.getBlockPos().equals(new BlockPos(pos.getX(), newY, pos.getZ()))){
                                    player.teleport(pos.getX() + 0.5, newY, pos.getZ() + 0.5);
                                    world.playSound(null, player.getBlockPos(), world.getBlockState(pos).getBlock().getSoundGroup(world.getBlockState(pos)).getStepSound(), SoundCategory.BLOCKS, 0.25f, 1.0f);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
