package fr.zabricraft.delta.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    public ItemMoveCallback(ItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    public boolean isLongPressDragEnabled() {
        return true;
    }

    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    }

    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            mAdapter.onRowSelected(viewHolder);
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        mAdapter.onRowClear(viewHolder);
    }

    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        return mAdapter.canDropOver(target.getAdapterPosition());
    }

    public interface ItemTouchHelperContract {

        boolean canDropOver(int toPosition);

        boolean onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(RecyclerView.ViewHolder viewHolder);

        void onRowClear(RecyclerView.ViewHolder viewHolder);

    }

}
